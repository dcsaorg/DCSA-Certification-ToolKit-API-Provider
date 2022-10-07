package org.dcsa.api.validator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dcsa.api.validator.model.enums.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class TNTEventSubscriptionTO extends AbstractEventSubscription {

    private List<EventType> eventType;

    private List<ShipmentEventTypeCode> shipmentEventTypeCode;

    private String carrierBookingReference;

    private String transportDocumentReference;

    private List<TransportDocumentTypeCode> transportDocumentTypeCode;

    private List<TransportEventTypeCode> transportEventTypeCode;

    private String transportCallID;

    private String vesselIMONumber;

    private String carrierVoyageNumber;

    private String carrierServiceCode;

    private List<EquipmentEventTypeCode> equipmentEventTypeCode;

    private String equipmentReference;
}
