package eu.dzhw.fdz.metadatamanagement.ordermanagement.rest;

import eu.dzhw.fdz.metadatamanagement.AbstractTest;
import eu.dzhw.fdz.metadatamanagement.common.domain.I18nString;
import eu.dzhw.fdz.metadatamanagement.common.rest.TestUtil;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Customer;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Order;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderClient;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderState;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.OrderedStudy;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.domain.Product;
import eu.dzhw.fdz.metadatamanagement.ordermanagement.repository.OrderRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderResourceTest extends AbstractTest {

  private static final String UPDATE_ORDER_URL = "/api/orders/";

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private OrderRepository orderRepository;

  private MockMvc mockMvc;


  @Before
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
        .build();
  }

  @After
  public void cleanUp() {
    this.orderRepository.deleteAll();
  }

  @Test
  public void updateOrder() throws Exception {
    Order order = createOrder();
    order = orderRepository.save(order);
    order.getProducts().add(createProduct(order.getId()));

    mockMvc.perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isOk())
        .andExpect(header().exists("Location"));
  }

  @Test
  public void updateOrder_optimistic_locking() throws Exception {
    Order order = createOrder();
    order = orderRepository.save(order);
    order.getProducts().add(createProduct(order.getId()));
    order.setVersion(-1L);

    mockMvc.perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void updateOrder_already_ordered() throws Exception {
    Order order = createOrder();
    order.setState(OrderState.ORDERED);
    order = orderRepository.save(order);
    order.getProducts().add(createProduct(order.getId()));

    mockMvc.perform(put(UPDATE_ORDER_URL + order.getId()).contentType(MediaType.APPLICATION_JSON)
        .content(TestUtil.convertObjectToJsonBytes(order)))
        .andExpect(status().isBadRequest());
  }

  private Order createOrder() {
    Order order = new Order();
    order.setState(OrderState.CREATED);
    order.setLanguageKey("de");
    order.setClient(OrderClient.MDM);
    order.setProducts(new ArrayList<>());

    Customer customer = new Customer("Test User", "test@devnull.com");
    order.setCustomer(customer);

    return order;
  }

  private Product createProduct(String dataAcquisitionProjectId) {
    OrderedStudy study = new OrderedStudy();
    study.setId("stu-" + dataAcquisitionProjectId + "$");
    I18nString title = new I18nString("test", "test");
    study.setTitle(title);
    Product product = new Product(dataAcquisitionProjectId, study, "remote-desktop-suf", "1.0.0");
    return product;
  }
}