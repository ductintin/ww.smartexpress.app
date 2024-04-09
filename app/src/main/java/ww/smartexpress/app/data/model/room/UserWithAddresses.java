package ww.smartexpress.app.data.model.room;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWithAddresses {
    @Embedded
    UserEntity user;
    @Relation(
            parentColumn = "user_id",
            entityColumn = "userId"
    )

    List<AddressEntity> addresses;
}
