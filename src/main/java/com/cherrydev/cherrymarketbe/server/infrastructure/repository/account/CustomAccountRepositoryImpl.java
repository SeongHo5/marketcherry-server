package com.cherrydev.cherrymarketbe.server.infrastructure.repository.account;

import com.cherrydev.cherrymarketbe.server.domain.account.entity.Account;
import com.cherrydev.cherrymarketbe.server.domain.account.entity.QAccount;
import com.cherrydev.cherrymarketbe.server.domain.account.enums.*;
import com.cherrydev.cherrymarketbe.server.domain.admin.dto.request.AccountSearchConditions;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomAccountRepositoryImpl implements CustomAccountRepository {

    private static final QAccount qAccount = QAccount.account;

    private final JPAQueryFactory jpaQueryFactory;

    public CustomAccountRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Account> findByConditions(Pageable pageable, AccountSearchConditions conditions) {
        // 쿼리 생성 및 엔티티 조회
        List<Account> result = jpaQueryFactory.selectFrom(qAccount)
                .where(
                        emailLike(conditions.email()),
                        nameLike(conditions.name()),
                        genderEquals(conditions.gender()),
                        registerTypeEquals(conditions.registerType()),
                        statusEquals(conditions.status()),
                        roleEquals(conditions.role())
                )
                .orderBy(determineOrderSpecification(conditions.sort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        // totalCount 계산
        Long totalCount = jpaQueryFactory
                .select(qAccount.count())
                .from(qAccount)
                .where(
                        emailLike(conditions.email()),
                        nameLike(conditions.name()),
                        genderEquals(conditions.gender()),
                        registerTypeEquals(conditions.registerType()),
                        statusEquals(conditions.status()),
                        roleEquals(conditions.role())
                ).fetchFirst();

        return new PageImpl<>(result, pageable, totalCount);
    }

    private BooleanExpression emailLike(@Nullable String email) {
        if (email != null) {
            return qAccount.email.likeIgnoreCase("%" + email + "%");
        }
        return null;
    }

    private BooleanExpression nameLike(@Nullable String name) {
        if (name != null) {
            return qAccount.name.likeIgnoreCase("%" + name + "%");
        }
        return null;
    }

    private BooleanExpression genderEquals(@Nullable String gender) {
        if (gender != null) {
            return qAccount.gender.eq(Gender.valueOf(gender));
        }
        return null;
    }

    private BooleanExpression registerTypeEquals(@Nullable String registerType) {
        if (registerType != null) {
            return qAccount.registType.eq(RegisterType.valueOf(registerType));
        }
        return null;
    }

    private BooleanExpression statusEquals(@Nullable String status) {
        if (status != null) {
            return qAccount.userStatus.eq(UserStatus.valueOf(status));
        }
        return null;
    }

    private BooleanExpression roleEquals(@Nullable String role) {
        if (role != null) {
            return qAccount.userRole.eq(UserRole.valueOf(role));
        }
        return null;
    }

    private OrderSpecifier<?> determineOrderSpecification(@Nullable String sort) {
        OrderSpecifier<?> defaultOrder = qAccount.createdAt.desc();
        if (sort != null) {
            if (AccountSortType.REGISTER_DATE_DESC.isEquals(sort)) {
                return qAccount.createdAt.desc();
            }
            if (AccountSortType.NAME_ASC.isEquals(sort)) {
                return qAccount.name.asc();
            }
            if (AccountSortType.NAME_DESC.isEquals(sort)) {
                return qAccount.name.desc();
            }
            if (AccountSortType.EMAIL_ASC.isEquals(sort)) {
                return qAccount.email.asc();
            }
            if (AccountSortType.EMAIL_DESC.isEquals(sort)) {
                return qAccount.email.desc();
            }
        }
        return defaultOrder;
    }

}
