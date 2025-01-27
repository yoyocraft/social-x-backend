package com.youyi.domain.user.model.relation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/01/27
 */
@Getter
@Setter
@RelationshipProperties
public class UserRelationship {

    @Id
    @GeneratedValue
    private Long id;

    @TargetNode
    private UserNode target;

    @Property
    private Long since;
}
