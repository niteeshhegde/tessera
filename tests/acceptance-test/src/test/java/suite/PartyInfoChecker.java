package suite;

import com.quorum.tessera.config.CommunicationType;
import com.quorum.tessera.reflect.ReflectCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface PartyInfoChecker {

    Logger LOGGER = LoggerFactory.getLogger(PartyInfoChecker.class);

    boolean hasSynced();

    static PartyInfoChecker create(CommunicationType communicationType) {
        LOGGER.info("Creating checker for {}", communicationType);
        if (communicationType == CommunicationType.GRPC) {
            return (PartyInfoChecker) ReflectCallback.execute(() ->
                Class.forName("suite.GrpcPartyInfoCheck").getConstructor()
                    .newInstance());
        }
        return new RestPartyInfoChecker();
    }
}
