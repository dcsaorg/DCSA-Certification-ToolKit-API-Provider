package org.dcsa.api.provider.ctk.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import org.dcsa.api.provider.ctk.model.enums.EventType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class AbstractEventSubscription {
    protected UUID subscriptionID;
    protected String callbackUrl;
    @JsonProperty(
            access = Access.WRITE_ONLY
    )
    protected byte[] secret;

    public abstract List<EventType> getEventType();

    public AbstractEventSubscription() {
    }

    public UUID getSubscriptionID() {
        return this.subscriptionID;
    }

    public String getCallbackUrl() {
        return this.callbackUrl;
    }

    public byte[] getSecret() {
        return this.secret;
    }


    public void setSubscriptionID(final UUID subscriptionID) {
        this.subscriptionID = subscriptionID;
    }

    public void setCallbackUrl(final String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }


    @JsonProperty(
            access = Access.WRITE_ONLY
    )
    public void setSecret(final byte[] secret) {
        this.secret = secret;
    }

        public String toString() {
        UUID var10000 = this.getSubscriptionID();
        return "AbstractEventSubscription(subscriptionID=" + var10000 + ", callbackUrl=" + this.getCallbackUrl() + ", secret=" + Arrays.toString(this.getSecret()) + ")";
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof AbstractEventSubscription)) {
            return false;
        } else {
            AbstractEventSubscription other = (AbstractEventSubscription)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                label41: {
                    Object this$subscriptionID = this.getSubscriptionID();
                    Object other$subscriptionID = other.getSubscriptionID();
                    if (this$subscriptionID == null) {
                        if (other$subscriptionID == null) {
                            break label41;
                        }
                    } else if (this$subscriptionID.equals(other$subscriptionID)) {
                        break label41;
                    }

                    return false;
                }

                Object this$callbackUrl = this.getCallbackUrl();
                Object other$callbackUrl = other.getCallbackUrl();
                if (this$callbackUrl == null) {
                    if (other$callbackUrl != null) {
                        return false;
                    }
                } else if (!this$callbackUrl.equals(other$callbackUrl)) {
                    return false;
                }

        /*        if (!Arrays.equals(this.getSecret(), other.getSecret())) {
                    return false;
                } else {
                    return true;
                }*/
            }
        }
        return  true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AbstractEventSubscription;
    }

    public int hashCode() {
        boolean PRIME = true;
        int result = super.hashCode();
        Object $subscriptionID = this.getSubscriptionID();
        result = result * 59 + ($subscriptionID == null ? 43 : $subscriptionID.hashCode());
        Object $callbackUrl = this.getCallbackUrl();
        result = result * 59 + ($callbackUrl == null ? 43 : $callbackUrl.hashCode());
     //   result = result * 59 + Arrays.hashCode(this.getSecret());
        return result;
    }
}
