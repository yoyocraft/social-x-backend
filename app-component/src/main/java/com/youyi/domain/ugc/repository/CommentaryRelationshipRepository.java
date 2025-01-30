package com.youyi.domain.ugc.repository;

import com.youyi.domain.ugc.repository.relation.CommentaryNode;
import com.youyi.domain.ugc.repository.relation.UgcInteractRelationship;
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
public interface CommentaryRelationshipRepository extends Neo4jRepository<CommentaryNode, Long> {

    @Query("CREATE (c:commentary {commentaryId: $commentaryId}) RETURN c")
    CommentaryNode save(@Param("commentaryId") String commentaryId);

    @Query(
        """ 
            MATCH (u:user {userId: $userId})
            WITH u
            MATCH (t:commentary {commentaryId: $commentaryId})
            MERGE (u)-[r:LIKE]->(t)
            SET r.since = timestamp()
            """
    )
    void addLikeRelationship(@Param("commentaryId") String commentaryId, @Param("userId") String userId);

    @Query(
        """
            MATCH (u:user {userId: $userId})-[r:LIKE]->(t:commentary {commentaryId: $commentaryId})
            DELETE r
            """
    )
    void deleteLikeRelationship(@Param("commentaryId") String commentaryId, @Param("userId") String userId);

    @Query("MATCH (u:user {userId: $userId})-[r:LIKE]->(t:commentary {commentaryId: $commentaryId}) RETURN u AS target, r.since AS since")
    UgcInteractRelationship queryLikeRelationship(@Param("commentaryId") String commentaryId, @Param("userId") String userId);

    @Query("MATCH (c:commentary {commentaryId: $commentaryId}) RETURN c")
    CommentaryNode findByCommentaryId(@Param("commentaryId") String commentaryId);

    @Query("""
            MATCH (u:user)-[r:LIKE]->(t:commentary {commentaryId: $commentaryId})
            DELETE r
        """
    )
    void deleteAllLikeRelationships(@Param("commentaryId") String commentaryId);

    @Query("""
        MATCH (u:user)-[r:LIKE]->(t:commentary)
        WHERE t.commentaryId IN $commentaryIds
        DELETE r
        """
    )
    void deleteAllLikeRelationships(@Param("commentaryIds") List<String> commentaryIds);

    @Query("""
        MATCH (t:CommentaryNode)
        WHERE t.commentaryId IN $commentaryIds
        DETACH DELETE t;
        """)
    void deleteCommentaryNode(@Param("commentaryIds") List<String> commentaryIds);
}
