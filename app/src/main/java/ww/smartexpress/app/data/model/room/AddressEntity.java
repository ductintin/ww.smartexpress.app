package ww.smartexpress.app.data.model.room;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(tableName = "address")
public class AddressEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "address_id")
    private Long id;
    private String place_id;
    private String main_text;
    private String description;
    private Long userId;
}
