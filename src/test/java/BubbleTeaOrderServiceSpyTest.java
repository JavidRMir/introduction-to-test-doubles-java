import com.techreturners.bubbleteaordersystem.model.*;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaMessenger;
import com.techreturners.bubbleteaordersystem.service.BubbleTeaOrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testhelper.DummySimpleLogger;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class BubbleTeaOrderServiceSpyTest {

    private DebitCard testDebitCard;
    private PaymentDetails paymentDetails;
    private DummySimpleLogger dummySimpleLogger;
    private BubbleTeaMessenger spiedMessenger;
    private BubbleTeaOrderService bubbleTeaOrderService;

    @BeforeEach
    public void setup() {
        testDebitCard = new DebitCard("0123456789");
        paymentDetails = new PaymentDetails("hello kitty", "sanrio puroland", testDebitCard);
        dummySimpleLogger = new DummySimpleLogger();
        spiedMessenger = spy(new BubbleTeaMessenger(dummySimpleLogger));
        bubbleTeaOrderService = new BubbleTeaOrderService(dummySimpleLogger, spiedMessenger);
    }

    @Test
    public void shouldCreateBubbleTeaOrderRequestWhenCreateOrderRequestIsCalled() {

        //Arrange
        BubbleTea bubbleTea = new BubbleTea(BubbleTeaTypeEnum.MatchaMilkTea, 6.78);
        BubbleTeaRequest bubbleTeaRequest = new BubbleTeaRequest(paymentDetails, bubbleTea);

        BubbleTeaOrderRequest expectedResult = new BubbleTeaOrderRequest(
                "hello kitty",
                "sanrio puroland",
                "0123456789",
                BubbleTeaTypeEnum.MatchaMilkTea
        );

        //Act
        BubbleTeaOrderRequest result = bubbleTeaOrderService.createOrderRequest(bubbleTeaRequest);

        //Assert
        Assertions.assertEquals(expectedResult.getName(), result.getName());
        Assertions.assertEquals(expectedResult.getAddress(), result.getAddress());
        Assertions.assertEquals(expectedResult.getDebitCardDigits(), result.getDebitCardDigits());
        Assertions.assertEquals(expectedResult.getBubbleTeaType(), result.getBubbleTeaType());

        //Check the spied messenger was called with BubbleTeaOrderRequest result
        verify(spiedMessenger).sendBubbleTeaOrderRequestEmail(result);
        verify(spiedMessenger, times(1)).sendBubbleTeaOrderRequestEmail(result);
    }


    @Test
    public void spyMapToCheckEachInteractionWithCreateBubbleTeaOrderRequest() {

        Map<String, BubbleTeaOrderRequest> spyMap = spy(new HashMap<>());

        BubbleTea bubbleTeaOne = new BubbleTea(BubbleTeaTypeEnum.MatchaMilkTea, 6.78);
        BubbleTeaRequest bubbleTeaRequestOne = new BubbleTeaRequest(paymentDetails, bubbleTeaOne);

        BubbleTea bubbleTeaTwo = new BubbleTea(BubbleTeaTypeEnum.OolongMilkTea, 5.69);
        BubbleTeaRequest bubbleTeaRequestTwo = new BubbleTeaRequest(paymentDetails, bubbleTeaTwo);

        BubbleTeaOrderRequest result1 = bubbleTeaOrderService.createOrderRequest(bubbleTeaRequestOne);
        BubbleTeaOrderRequest result2 = bubbleTeaOrderService.createOrderRequest(bubbleTeaRequestTwo);

        spyMap.put("RequestOne", result1);
        spyMap.put("RequestTwo", result2);

        verify(spyMap).put("RequestOne", result1);
        verify(spyMap).put("RequestTwo", result2);

        Assertions.assertEquals(2, spyMap.size());

        Assertions.assertEquals(BubbleTeaTypeEnum.MatchaMilkTea,
                spyMap.get("RequestOne").getBubbleTeaType());

        Assertions.assertEquals(BubbleTeaTypeEnum.OolongMilkTea,
                spyMap.get("RequestTwo").getBubbleTeaType());


    }


}
