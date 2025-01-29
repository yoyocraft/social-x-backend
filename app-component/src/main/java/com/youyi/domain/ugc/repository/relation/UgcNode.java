package com.youyi.domain.ugc.repository.relation;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Getter
@Setter
@Node(labels = "ugc")
public class UgcNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property(value = "ugcId")
    private String ugcId;

    /**
     * user --> ugc
     */
    @Relationship(type = "LIKE", direction = Relationship.Direction.INCOMING)
    private List<UgcLikeRelationship> likes;
}
