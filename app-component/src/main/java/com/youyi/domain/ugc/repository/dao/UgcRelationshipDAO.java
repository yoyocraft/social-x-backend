package com.youyi.domain.ugc.repository.dao;

import com.youyi.domain.ugc.repository.relation.UgcInteractInfo;
import com.youyi.domain.ugc.repository.relation.UgcInteractRelationship;
import com.youyi.domain.ugc.repository.relation.UgcNode;
import java.util.List;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Repository
public interface UgcRelationshipDAO extends Neo4jRepository<UgcNode, Long> {

    @Query("CREATE (u:ugc {ugcId: $ugcId}) RETURN u")
    UgcNode save(@Param("ugcId") String ugcId);

    @Query(
        """ 
            MATCH (u:user {userId: $userId})
            WITH u
            MATCH (t:ugc {ugcId: $ugcId})
            MERGE (u)-[r:LIKE]->(t)
            SET r.since = timestamp()
            """
    )
    void addLikeRelationship(@Param("ugcId") String ugcId, @Param("userId") String userId);

    @Query(
        """
            MATCH (u:user {userId: $userId})-[r:LIKE]->(t:ugc {ugcId: $ugcId})
            DELETE r
            """
    )
    void deleteLikeRelationship(@Param("ugcId") String ugcId, @Param("userId") String userId);

    @Query(
        """ 
            MATCH (u:user {userId: $userId})
            WITH u
            MATCH (t:ugc {ugcId: $ugcId})
            MERGE (u)-[r:COLLECT]->(t)
            SET r.since = timestamp()
            """
    )
    void addCollectRelationship(@Param("ugcId") String ugcId, @Param("userId") String userId);

    @Query(
        """
            MATCH (u:user {userId: $userId})-[r:COLLECT]->(t:ugc {ugcId: $ugcId})
            DELETE r
            """
    )
    void deleteCollectRelationship(@Param("ugcId") String ugcId, @Param("userId") String userId);

    @Query("MATCH (u:user {userId: $userId})-[r:LIKE]->(t:ugc {ugcId: $ugcId}) RETURN u AS target, r.since AS since")
    UgcInteractRelationship queryLikeRelationship(@Param("ugcId") String ugcId, @Param("userId") String userId);

    @Query("MATCH (u:user {userId: $userId})-[r:LIKE]->(t:ugc) WHERE t.ugcId IN $ugcIds RETURN t.ugcId")
    List<String> queryLikeRelationships(@Param("ugcIds") List<String> ugcIds, @Param("userId") String userId);

    @Query("MATCH (u:user {userId: $userId})-[r:COLLECT]->(t:ugc {ugcId: $ugcId}) RETURN u AS target, r.since AS since")
    UgcInteractRelationship queryCollectRelationship(@Param("ugcId") String ugcId, @Param("userId") String userId);

    @Query("MATCH (u:user {userId: $userId})-[r:COLLECT]->(t:ugc) WHERE t.ugcId IN $ugcIds RETURN t.ugcId")
    List<String> queryCollectRelationships(@Param("ugcIds") List<String> ugcIds, @Param("userId") String userId);

    @Query("""
        MATCH (u:user {userId: $userId})-[r:COLLECT]->(t:ugc)
                WHERE ($cursor IS NULL OR r.since < $cursor)
                RETURN t.ugcId AS ugcId, r.since AS since ORDER BY r.since DESC LIMIT $limit
        """)
    List<UgcInteractInfo> queryCollectedUgcIdsWithCursor(@Param("userId") String userId, @Param("cursor") Long cursor, @Param("limit") int limit);

    @Query("""
        MATCH (u:user {userId: $userId})-[r:COLLECT]->(t:ugc)
        RETURN t.ugcId AS ugcId
        """)
    List<String> queryAllCollectedUgcIds(@Param("userId") String userId);

    @Query("MATCH (u:ugc {ugcId: $ugcId}) RETURN u")
    UgcNode findByUgcId(@Param("ugcId") String ugcId);

    @Query("""
            MATCH (u:user)-[r:LIKE]->(t:ugc {ugcId: $ugcId})
            DELETE r
        """
    )
    void deleteAllLikeRelationships(@Param("ugcId") String ugcId);

    @Query("""
            MATCH (u:user)-[r:COLLECT]->(t:ugc {ugcId: $ugcId})
            DELETE r
        """
    )
    void deleteAllCollectRelationships(@Param("ugcId") String ugcId);

    @Query("""
        MATCH (t:ugc {ugcId: $ugcId})
        DELETE t
        """
    )
    void deleteUgcNode(@Param("ugcId") String ugcId);
}
