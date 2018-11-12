package no.nordicsemi.android.meshprovisioner.data;

import android.arch.persistence.room.Relation;

import java.util.List;

import no.nordicsemi.android.meshprovisioner.MeshNetwork;
import no.nordicsemi.android.meshprovisioner.transport.ApplicationKey;
import no.nordicsemi.android.meshprovisioner.transport.NetworkKey;

@SuppressWarnings("unused")
class ApplicationKeys {

    public String uuid;

    @Relation(entity = MeshNetwork.class, parentColumn = "mesh_uuid", entityColumn = "uuid")
    public List<ApplicationKey> applicationKeys;

}
