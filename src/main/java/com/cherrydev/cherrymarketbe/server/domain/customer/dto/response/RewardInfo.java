package com.cherrydev.cherrymarketbe.server.domain.customer.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RewardInfo {

    List<RewardItem> rewards;
    RewardSummary summary;

    @Getter
    @Builder
    public static class RewardItem {
        String rewardGrantType;
        Integer amounts;
        String earnedAt;
        String expiredAt;
        Boolean isUsed;
        String usedAt;
    }

    @Getter
    @Builder
    public static class RewardSummary {
        Integer totalRewards;
        Integer usedRewards;
        Integer availableRewards;
        Integer expiredRewards;
    }

}
