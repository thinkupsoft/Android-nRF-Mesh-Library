/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.meshprovisioner.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import no.nordicsemi.android.meshprovisioner.message.type.AccessMessage;
import no.nordicsemi.android.meshprovisioner.message.type.ControlMessage;
import no.nordicsemi.android.meshprovisioner.message.type.Message;

/**
 * This class handles binding application keys to a specific model where the mode could be,
 * a 16-bit Bluetooth SigModel or a 32-bit Vendor Model
 */
final class ConfigModelAppUnbindState extends ConfigMessageState {

    private static final String TAG = ConfigModelAppUnbindState.class.getSimpleName();


    ConfigModelAppUnbindState(@NonNull final Context context,
                                     @NonNull final ConfigModelAppUnbind configModelAppUnbind,
                                     @NonNull final MeshTransport meshTransport,
                                     @NonNull final InternalMeshMsgHandlerCallbacks callbacks) {
        super(context, configModelAppUnbind, meshTransport, callbacks);
        createAccessMessage();
    }

    @Override
    public MessageState getState() {
        return MessageState.CONFIG_MODEL_APP_UNBIND_STATE;
    }

    /**
     * Creates the access message to be sent to the node
     */
    private void createAccessMessage() {
        final ConfigModelAppUnbind configModelAppUnbind = (ConfigModelAppUnbind) mMeshMessage;
        final byte[] key = mNode.getDeviceKey();
        final int akf = configModelAppUnbind.getAkf();
        final int aid = configModelAppUnbind.getAid();
        final int aszmic = configModelAppUnbind.getAszmic();
        final int opCode = configModelAppUnbind.getOpCode();
        final byte[] parameters = configModelAppUnbind.getParameters();
        message = mMeshTransport.createMeshMessage(mNode, mSrc, key, akf, aid, aszmic, opCode, parameters);
        configModelAppUnbind.setMessage(message);
        mPayloads.putAll(message.getNetworkPdu());
    }

    @Override
    public final void executeSend() {
        Log.v(TAG, "Sending config app unbind");
        super.executeSend();

        if (!mPayloads.isEmpty()) {
            if (mMeshStatusCallbacks != null)
                mMeshStatusCallbacks.onAppKeyUnbindSent(mNode);
        }
    }
}
