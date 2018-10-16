package no.nordicsemi.android.meshprovisioner.message;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.message.type.AccessMessage;
import no.nordicsemi.android.meshprovisioner.message.type.ControlMessage;
import no.nordicsemi.android.meshprovisioner.message.type.Message;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.MeshParserUtils;

/**
 * State class for handling GenericLevelGet messages.
 */
class GenericLevelGetState extends GenericMessageState {

    private static final String TAG = GenericLevelGetState.class.getSimpleName();

    /**
     * Constructs GenericLevelGetState
     *
     * @param context         Context of the application
     * @param dstAddress      Destination address to which the message must be sent to
     * @param genericLevelGet Wrapper class {@link GenericLevelGet} containing the opcode and parameters for {@link GenericLevelGet} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException for any illegal arguments provided.
     */
    GenericLevelGetState(@NonNull final Context context,
                                @NonNull final byte[] dstAddress,
                                @NonNull final GenericLevelGet genericLevelGet,
                                @NonNull final MeshTransport meshTransport,
                                @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, dstAddress, genericLevelGet, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.GENERIC_LEVEL_GET_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final GenericLevelGet genericLevelGet = (GenericLevelGet) mMeshMessage;
        final byte[] key = genericLevelGet.getAppKey();
        final int akf = genericLevelGet.getAkf();
        final int aid = genericLevelGet.getAid();
        final int aszmic = genericLevelGet.getAszmic();
        final int opCode = genericLevelGet.getOpCode();
        final byte[] parameters = genericLevelGet.getParameters();
        message = mMeshTransport.createMeshMessage(mNode, mSrc, mDstAddress, key, akf, aid, aszmic, opCode, parameters);
        genericLevelGet.setMessage(message);
        mPayloads.putAll(message.getNetworkPdu());
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending Generic Level get");
        super.executeSend();

        if (!mPayloads.isEmpty()) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onGenericLevelGetSent(mNode);
        }
    }
}
