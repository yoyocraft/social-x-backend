package com.youyi.domain.user.repository.relation;

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
 * @date 2025/01/27
 */
@Getter
@Setter
@Node(labels = "user")
public class UserNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property(value = "userId")
    private String userId;

    @Property(value = "nickname")
    private String nickname;

    /**
     * 关注的用户
     * <p>
     * fan --> up
     */
    @Relationship(type = "FOLLOWING", direction = Relationship.Direction.OUTGOING)
    private List<UserRelationship> followingUsers;

}
