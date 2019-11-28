package no.nordicsemi.android.meshprovisioner.transport;

import androidx.annotation.NonNull;

/**
 * Abstract wrapper class for mesh message.
 */
public abstract class ProxyConfigStatusMessage extends MeshMessage {

    public ProxyConfigStatusMessage(@NonNull final ControlMessage message) {
        mMessage = message;
    }

    /**
     * Parses the status parameters returned by a status message
     */
    abstract void parseStatusParameters();

    @Override
    int getAid() {
        return -1;
    }

    @Override
    int getAkf() {
        return -1;
    }

}
