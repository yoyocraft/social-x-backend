package com.youyi.domain.user.core;

import com.youyi.BaseIntegrationTest;
import com.youyi.domain.user.model.UserDO;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/yoyocraft">yoyocraft</a>
 * @date 2025/04/11
 */
class UserServiceIntegrationTest extends BaseIntegrationTest {

    /**
     * 1883827647466573824
     * 1883829503093772288
     * <p>
     * 1910588312219815936
     * 1910589040149663744
     * 1910589040174829568
     * 1910589040179023872
     * <p>
     * 1910589040179023873
     * 1910589040183218176
     * 1910589040183218177
     * 1910589040183218178
     * 1910589040187412480
     * 1910589040187412481
     * 1910589040187412482
     * 1910589040191606784
     * 1910589040191606785
     */
    List<Pair<String /* fan */, String /* up */>> followData = new ArrayList<>();

    {
        // 1883827647466573824 -> *
        followData.add(Pair.of("1883827647466573824", "1883829503093772288"));

        // 1883827647466573824 -> 1883829503093772288 -> *
        followData.add(Pair.of("1883829503093772288", "1910588312219815936"));
        followData.add(Pair.of("1883829503093772288", "1910589040149663744"));
        followData.add(Pair.of("1883829503093772288", "1910589040174829568"));
        followData.add(Pair.of("1883829503093772288", "1910589040179023872"));

        // // 1883827647466573824 -> 1883829503093772288 -> 1910588312219815936 -> *
        followData.add(Pair.of("1910588312219815936", "1910589040179023873"));
        followData.add(Pair.of("1910588312219815936", "1910589040183218176"));
        followData.add(Pair.of("1910588312219815936", "1910589040183218177"));
        followData.add(Pair.of("1910588312219815936", "1910589040183218178"));
        followData.add(Pair.of("1910588312219815936", "1910589040187412480"));
        followData.add(Pair.of("1910588312219815936", "1910589040187412481"));
        followData.add(Pair.of("1910588312219815936", "1910589040191606784"));
        followData.add(Pair.of("1910588312219815936", "1910589040191606785"));
    }

    @Autowired
    UserService userService;

    @Test
    void testFollow() {
        for (Pair<String, String> pair : followData) {
            String fanId = pair.getLeft();
            String upId = pair.getRight();

            UserDO fan = userService.queryByUserId(fanId);
            UserDO up = userService.queryByUserId(upId);

            userService.followUser(fan, up);
        }
    }

}
