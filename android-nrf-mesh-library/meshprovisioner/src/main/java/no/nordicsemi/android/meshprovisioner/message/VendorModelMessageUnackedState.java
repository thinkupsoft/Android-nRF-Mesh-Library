package no.nordicsemi.android.meshprovisioner.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.message.type.ControlMessage;
import no.nordicsemi.android.meshprovisioner.message.type.Message;

class VendorModelMessageUnackedState extends GenericMessageState {

    private static final String TAG = VendorModelMessageUnackedState.class.getSimpleName();

    /**
     * Constructs {@link VendorModelMessageAckedState}
     *
     * @param context         Context of the application
     * @param dstAddress      Destination address to which the message must be sent to
     * @param vendorModelMessageUnacked Wrapper class {@link VendorModelMessageStatus} containing the opcode and parameters for {@link VendorModelMessageStatus} message
     * @param callbacks       {@link InternalMeshMsgHandlerCallbacks} for internal callbacks
     * @throws IllegalArgumentException exception for invalid arguments
     */
    VendorModelMessageUnackedState(@NonNull final Context context,
                                          @NonNull final byte[] dstAddress,
                                          @NonNull final VendorModelMessageUnacked vendorModelMessageUnacked,
                                          @NonNull final MeshTransport meshTransport,
                                          @NonNull final InternalMeshMsgHandlerCallbacks callbacks) throws IllegalArgumentException {
        super(context, dstAddress, vendorModelMessageUnacked, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MeshMessageState.MessageState getState() {
        return MessageState.VENDOR_MODEL_UNACKNOWLEDGED_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final VendorModelMessageUnacked vendorModelMessageUnacked = (VendorModelMessageUnacked) mMeshMessage;
        final byte[] src = vendorModelMessageUnacked.getMeshNode().getConfigurationSrc();
        final byte[] key = vendorModelMessageUnacked.getAppKey();
        final int akf = vendorModelMessageUnacked.getAkf();
        final int aid = vendorModelMessageUnacked.getAid();
        final int aszmic = vendorModelMessageUnacked.getAszmic();
        final int opCode = vendorModelMessageUnacked.getOpCode();
        final byte[] parameters = vendorModelMessageUnacked.getParameters();
        final int companyIdentifier = vendorModelMessageUnacked.getCompanyIdentifier();
        message = mMeshTransport.createVendorMeshMessage(mNode, companyIdentifier, src, mDstAddress, key, akf, aid, aszmic, opCode, parameters);
        vendorModelMessageUnacked.setMessage(message);
        mPayloads.putAll(message.getNetworkPdu());
    }

    @Override
    public void executeSend() {
        Log.v(TAG, "Sending acknowledged vendor model message");
        super.executeSend();
    }
}
