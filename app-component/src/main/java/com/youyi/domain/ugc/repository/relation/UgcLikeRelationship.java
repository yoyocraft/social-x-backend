package com.youyi.domain.ugc.repository.relation;

import com.youyi.domain.user.repository.relation.UserNode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/29
 */
@Getter
@Setter
@RelationshipProperties
public class UgcLikeRelationship {

    @Id
    @GeneratedValue
    private Long id;

    @Property(value = "since")
    private Long since;

    @TargetNode
    private UserNode target;
}
