package no.nordicsemi.android.meshprovisioner.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import no.nordicsemi.android.meshprovisioner.transport.ProvisionedMeshNode;

@SuppressWarnings("unused")
@Dao
public interface ProvisionedMeshNodesDao {

    @Query("SELECT * from nodes WHERE mesh_uuid == :meshUuid")
    List<ProvisionedMeshNode> loadMeshNodes(final String meshUuid);
}
