package com.youyi.domain.user.repository;

import com.youyi.domain.user.repository.relation.UserNode;
import com.youyi.domain.user.repository.relation.UserRelationship;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Repository
public interface UserRelationRepository extends Neo4jRepository<UserNode, Long> {

    @Query("CREATE (u:user {userId: $userId, nickName: $nickName}) RETURN u")
    UserNode save(@Param("userId") String userId, @Param("nickName") String nickName);

    @Query("MATCH (u:user {userId: $userId}) RETURN u")
    UserNode findByUserId(@Param("userId") String userId);

    @Query("MATCH (u:user {userId: $userId})-[r:FOLLOWING]->(f:user) RETURN f AS target, r.since AS since")
    List<UserRelationship> queryFollowingUserRelations(@Param("userId") String userId);

    @Query("MATCH (u:user {userId: $userId})-[r:FOLLOWING]->(f:user {userId: $followingUserId}) RETURN f AS target, r.since AS since")
    UserRelationship queryFollowingUserRelations(@Param("userId") String userId, @Param("followingUserId") String followingUserId);

    @Query(
        """ 
            MATCH (f:user {userId: $userId})
            WITH f
            MATCH (t:user {userId: $followingUserId})
            MERGE (f)-[r:FOLLOWING]->(t)
            SET r.since = timestamp()
            """
    )
    void addFollowingUserRelationship(@Param("userId") String userId, @Param("followingUserId") String followingUserId);

    @Query(
        """
            MATCH (f:user {userId: $userId})-[r:FOLLOWING]->(t:user {userId: $followingUserId})
            DELETE r
            """
    )
    void deleteFollowingUserRelationship(@Param("userId") String userId, @Param("followingUserId") String followingUserId);

    @Query("""
            MATCH (u:user {userId: $userId})-[r:FOLLOWING]->(f:user)
            RETURN COUNT(r) AS followingCount
        """)
    int getFollowingCount(@Param("userId") String userId);

    // 查询用户的粉丝数量
    @Query("""
            MATCH (u:user)-[r:FOLLOWING]->(f:user {userId: $userId})
            RETURN COUNT(r) AS followerCount
        """)
    int getFollowerCount(@Param("userId") String userId);

    @Query("""
            MATCH (u:user {userId: $userId})-[r:FOLLOWING]->(f:user)
            WHERE f.userId > $lastUserId
            RETURN f AS target, r.since AS since
            ORDER BY f.since DESC, f.userId DESC
            LIMIT $pageSize
        """)
    List<UserRelationship> getFollowingUsers(
        @Param("userId") String userId,
        @Param("lastUserId") String lastUserId,
        @Param("pageSize") int pageSize
    );

    @Query("""
            MATCH (u:user)-[r:FOLLOWING]->(f:user {userId: $userId})
            WHERE u.userId > $lastUserId
            RETURN u AS target, r.since AS since
            ORDER BY u.since DESC, u.userId DESC
            LIMIT $pageSize
        """)
    List<UserRelationship> getFollowers(
        @Param("userId") String userId,
        @Param("lastUserId") String lastUserId,
        @Param("pageSize") int pageSize
    );
}
