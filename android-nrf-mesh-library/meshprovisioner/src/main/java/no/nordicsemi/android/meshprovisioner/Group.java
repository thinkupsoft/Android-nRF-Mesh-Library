package no.nordicsemi.android.meshprovisioner;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

import com.google.gson.annotations.Expose;

import java.util.Arrays;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Defines a group in a mesh network
 */
@SuppressWarnings("unused")
@Entity(tableName = "groups",
        foreignKeys = @ForeignKey(entity = MeshNetwork.class,
                parentColumns = "mesh_uuid",
                childColumns = "mesh_uuid",
                onUpdate = CASCADE, onDelete = CASCADE),
        indices = {@Index("mesh_uuid")})
public class Group {

    @ColumnInfo(name = "mesh_uuid")
    @Expose
    private String meshUuid;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "group_address")
    @Expose
    private byte[] groupAddress;

    @ColumnInfo(name = "parent_address")
    @Expose
    private byte[] parentAddress;

    /**
     * Constructs a mesh group
     *
     * @param groupAddress  groupAddress of the group
     * @param parentAddress parent address
     */
    public Group(@NonNull final byte[] groupAddress, @Nullable final byte[] parentAddress) {
        this.groupAddress = groupAddress;
        if (Arrays.equals(groupAddress, parentAddress)) {
            throw new IllegalArgumentException("Address cannot match parent adddress");
        }
        this.parentAddress = parentAddress;
    }

    /**
     * Returns the uuid of the network
     *
     * @return uuid
     */
    public String getMeshUuid() {
        return meshUuid;
    }

    /**
     * Sets the uuid of the network
     *
     * @param meshUuid network uuid
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void setMeshUuid(final String meshUuid) {
        this.meshUuid = meshUuid;
    }

    /**
     * Returns the group address
     *
     * @return 2 byte group address
     */
    public byte[] getGroupAddress() {
        return groupAddress;
    }

    /**
     * Sets a group address
     *
     * @param groupAddress 2 byte group address
     */
    public void setGroupAddress(@NonNull final byte[] groupAddress) {
        this.groupAddress = groupAddress;
    }

    /**
     * Returns address of the parent group if the group has one
     *
     * @return parent address
     */
    public byte[] getParentAddress() {
        return parentAddress;
    }

    /**
     * Sets the parent address, if this group belongs to a parent group
     *
     * @param parentAddress address of the parent group
     */
    public void setParentAddress(final byte[] parentAddress) {
        this.parentAddress = parentAddress;
    }
}
