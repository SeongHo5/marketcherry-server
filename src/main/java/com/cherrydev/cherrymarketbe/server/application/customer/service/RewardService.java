package com.cherrydev.cherrymarketbe.server.application.customer.service;

import com.cherrydev.cherrymarketbe.server.application.account.service.AccountQueryService;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.InsufficientRewardException;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.response.RewardInfo;
import com.cherrydev.cherrymarketbe.server.domain.customer.entity.CustomerReward;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.customer.CustomerRewardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.INSUFFICIENT_REWARD;
import static com.cherrydev.cherrymarketbe.server.domain.customer.enums.RewardGrantType.USE;

@Slf4j
@Service
@RequiredArgsConstructor
public class RewardService {

    private final Clock clock;
    private final AccountQueryService accountQueryService;
    private final CustomerRewardRepository customerRewardRepository;

    @Transactional
    public void grantReward(final RequestAddReward request) {
        Account account = accountQueryService.fetchAccountEntity(request.email());

        customerRewardRepository.save(CustomerReward.from(request, account));
    }

    @Transactional
    public void useReward(final Account account, final Integer usedReward) {
        List<CustomerReward> rewards = customerRewardRepository.findAllByAccount(account);
        int availableReward = caculateAvailableReward(rewards);

        validateRewardIsSufficient(availableReward, usedReward);

        applyRewardUsage(rewards, usedReward);
    }

    @Transactional(readOnly = true)
    public RewardInfo getRewardSummaryAndList(
            final AccountDetails accountDetails
    ) {
        Account account = accountQueryService.fetchAccountEntity(accountDetails.getUsername());
        List<CustomerReward> rewards = customerRewardRepository.findAllByAccount(account);

        RewardInfo.RewardSummary summary = generateRewardSummary(rewards);
        List<RewardInfo.RewardItem> rewardItems = rewards.stream()
                .map(this::generateRewardItem)
                .toList();

        return RewardInfo.builder()
                .summary(summary)
                .rewards(rewardItems)
                .build();
    }

    private RewardInfo.RewardSummary generateRewardSummary(final List<CustomerReward> rewards) {
        Integer totalReward = rewards.stream()
                .filter(reward -> !reward.getGrantType().isSameType(USE))
                .mapToInt(CustomerReward::getRewardAmount)
                .sum();

        Integer usedReward = rewards.stream()
                .filter(reward -> reward.getGrantType().isSameType(USE))
                .mapToInt(CustomerReward::getRewardAmount)
                .sum();

        Integer expiredReward = rewards.stream()
                .filter(credit -> !credit.getIsUsed() && credit.getExpiredAt().isBefore(LocalDate.now(clock)))
                .filter(reward -> !reward.getGrantType().isSameType(USE))
                .mapToInt(CustomerReward::getRewardAmount)
                .sum();

        Integer availabledReward = totalReward - usedReward - expiredReward;

        return RewardInfo.RewardSummary.builder()
                .totalRewards(totalReward)
                .availableRewards(availabledReward)
                .usedRewards(usedReward)
                .expiredRewards(expiredReward)
                .build();
    }

    private RewardInfo.RewardItem generateRewardItem(final CustomerReward reward) {
        return RewardInfo.RewardItem.builder()
                .rewardGrantType(reward.getGrantType().toString())
                .amounts(reward.getRewardAmount())
                .earnedAt(reward.getEarnedAt().toString())
                .expiredAt(reward.getExpiredAt().toString())
                .isUsed(reward.getIsUsed())
                .usedAt(reward.getUsedAt() == null
                        ? null
                        : reward.getUsedAt().toString())
                .build();
    }

    private int caculateAvailableReward(final List<CustomerReward> rewards) {
        return rewards.stream()
                .filter(reward -> !reward.getGrantType().isSameType(USE))
                .mapToInt(CustomerReward::getRewardAmount)
                .sum();
    }

    private void validateRewardIsSufficient(int availableReward, int usedReward) {
        if (availableReward < usedReward) {
            throw new InsufficientRewardException(INSUFFICIENT_REWARD);
        }
    }

    private void applyRewardUsage(final List<CustomerReward> rewards, int usedReward) {
        AtomicInteger usedRewardAtomic = new AtomicInteger(usedReward);
        rewards.stream()
                .filter(reward -> !reward.getGrantType().isSameType(USE))
                .forEach(reward -> {
                    int currentUsedReward = usedRewardAtomic.get();
                    if (currentUsedReward <= 0) {
                        return;
                    }

                    if (reward.getRewardAmount() <= currentUsedReward) {
                        reward.setIsUsed(true);
                        usedRewardAtomic.addAndGet(-reward.getRewardAmount());
                    } else {
//                        reward.useReward(currentUsedReward);
                        usedRewardAtomic.set(0);
                    }
                });
    }

}
