package no.nordicsemi.android.meshprovisioner.transport;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import no.nordicsemi.android.meshprovisioner.ApplicationKey;
import no.nordicsemi.android.meshprovisioner.opcodes.ApplicationMessageOpCodes;
import no.nordicsemi.android.meshprovisioner.utils.SecureUtils;

/**
 * To be used as a wrapper class when creating a GenericOnOffSet message.
 */
@SuppressWarnings("unused")
public class GenericOnOffSet extends GenericMessage {

    private static final String TAG = GenericOnOffSet.class.getSimpleName();
    private static final int OP_CODE = ApplicationMessageOpCodes.GENERIC_ON_OFF_SET;
    private static final int GENERIC_ON_OFF_SET_TRANSITION_PARAMS_LENGTH = 4;
    private static final int GENERIC_ON_OFF_SET_PARAMS_LENGTH = 2;

    private final Integer mTransitionSteps;
    private final Integer mTransitionResolution;
    private final Integer mDelay;
    private final boolean mState;
    private final int tId;

    /**
     * Constructs GenericOnOffSet message.
     *
     * @param appKey {@link ApplicationKey} key for this message
     * @param state  Boolean state of the GenericOnOffModel
     * @param tId    Transaction id
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public GenericOnOffSet(@NonNull final ApplicationKey appKey,
                           final boolean state,
                           final int tId) throws IllegalArgumentException {
        this(appKey, state, tId, null, null, null);
    }

    /**
     * Constructs GenericOnOffSet message.
     *
     * @param appKey               {@link ApplicationKey} key for this message
     * @param state                Boolean state of the GenericOnOffModel
     * @param tId                  Transaction id
     * @param transitionSteps      Transition steps for the level
     * @param transitionResolution Transition resolution for the level
     * @param delay                Delay for this message to be executed 0 - 1275 milliseconds
     * @throws IllegalArgumentException if any illegal arguments are passed
     */
    public GenericOnOffSet(@NonNull final ApplicationKey appKey,
                           final boolean state,
                           final int tId,
                           @Nullable final Integer transitionSteps,
                           @Nullable final Integer transitionResolution,
                           @Nullable final Integer delay) {
        super(appKey);
        this.mTransitionSteps = transitionSteps;
        this.mTransitionResolution = transitionResolution;
        this.mDelay = delay;
        this.mState = state;
        this.tId = tId;
        assembleMessageParameters();
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    @Override
    public void assembleMessageParameters() {
        mAid = SecureUtils.calculateK4(mAppKey.getKey());
        final ByteBuffer paramsBuffer;
        Log.v(TAG, "State: " + (mState ? "ON" : "OFF"));
        if (mTransitionSteps == null || mTransitionResolution == null || mDelay == null) {
            paramsBuffer = ByteBuffer.allocate(GENERIC_ON_OFF_SET_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
            paramsBuffer.put((byte) (mState ? 0x01 : 0x00));
            paramsBuffer.put((byte) tId);
        } else {
            Log.v(TAG, "Transition steps: " + mTransitionSteps);
            Log.v(TAG, "Transition step resolution: " + mTransitionResolution);
            paramsBuffer = ByteBuffer.allocate(GENERIC_ON_OFF_SET_TRANSITION_PARAMS_LENGTH).order(ByteOrder.LITTLE_ENDIAN);
            paramsBuffer.put((byte) (mState ? 0x01 : 0x00));
            paramsBuffer.put((byte) tId);
            paramsBuffer.put((byte) (mTransitionResolution << 6 | mTransitionSteps));
            final int delay = mDelay;
            paramsBuffer.put((byte) delay);
        }
        mParameters = paramsBuffer.array();

    }
}
