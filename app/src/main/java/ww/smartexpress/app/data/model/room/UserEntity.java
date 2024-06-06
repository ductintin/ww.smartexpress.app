package ww.smartexpress.app.data.model.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity(tableName = "user"
//        ,foreignKeys = @ForeignKey(entity = OrderEntity.class,parentColumns = "id",childColumns = "user_id",onDelete = CASCADE)
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String avatar;
    private Integer status;

    private String encryptedPassword;
}
