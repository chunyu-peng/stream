package space.gavinklfong.demo.streamapi;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lombok.extern.slf4j.Slf4j;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

@Slf4j
@DataJpaTest
public class StreamApiTest {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private ProductRepo productRepo;

	@Test
	@DisplayName("Obtain a list of product with category = \"Books\" and price > 100")
	public void exercise1() {
//		long startTime = System.currentTimeMillis();
//		List<Product> result = productRepo.findAll()
//		.stream()
//		.filter(p -> p.getCategory().equalsIgnoreCase("Books"))
//		.filter(p -> p.getPrice() > 100)
//		.collect(Collectors.toList());
//		long endTime = System.currentTimeMillis();
//
//		log.info(String.format("exercise 1 - execution time: %1$d ms", (endTime - startTime)));
//		result.forEach(p -> log.info(p.toString()));

		List<Product> res = productRepo.findAll()
				.stream()
				.filter(product->product.getCategory().equals("Books"))
				.filter(product->product.getPrice()>100)
				.toList();
		res.forEach(product-> log.info(product.toString()));
	}

	@Test
	@DisplayName("Obtain a list of product with category = \"Books\" and price > 100 (using Predicate chaining for filter)")
	public void exercise1a() {
//		Predicate<Product> categoryFilter = product -> product.getCategory().equalsIgnoreCase("Books");
//		Predicate<Product> priceFilter = product -> product.getPrice() > 100;
//
//		long startTime = System.currentTimeMillis();
//		List<Product> result = productRepo.findAll()
//				.stream()
//				.filter(product -> categoryFilter.and(priceFilter).test(product))
//				.collect(Collectors.toList());
//		long endTime = System.currentTimeMillis();
//
//		log.info(String.format("exercise 1a - execution time: %1$d ms", (endTime - startTime)));
//		result.forEach(p -> log.info(p.toString()));

		Predicate<Product> bookList = product->product.getCategory().equals("Books");
		Predicate<Product> bookPrice = product->product.getPrice()>100;

		List<Product> res = productRepo.findAll()
				.stream()
				.filter(product->bookList.and(bookPrice).test(product))
				.toList();
		res.forEach(product-> log.info(product.toString()));
	}

	@Test
	@DisplayName("Obtain a list of product with category = \"Books\" and price > 100 (using BiPredicate for filter)")
	public void exercise1b() {
//		BiPredicate<Product, String> categoryFilter = (product, category) -> product.getCategory().equalsIgnoreCase(category);
//
//		long startTime = System.currentTimeMillis();
//		List<Product> result = productRepo.findAll()
//				.stream()
//				.filter(product -> categoryFilter.test(product, "Books") && product.getPrice() > 100)
//				.collect(Collectors.toList());
//		long endTime = System.currentTimeMillis();
//
//		log.info(String.format("exercise 1b - execution time: %1$d ms", (endTime - startTime)));
//		result.forEach(p -> log.info(p.toString()));

		BiPredicate<Product, String> bookList = (product, category)->product.getCategory().equals("Books");
		BiPredicate<Product, Integer> bookPrice = (product,price)->product.getPrice()>price;

		List<Product> res = productRepo.findAll()
				.stream()
				.filter(product->bookList.test(product,"Books") && (bookPrice.test(product,100)))
				.toList();
		res.forEach(product-> log.info(product.toString()));
	}

	@Test
	@DisplayName("Obtain a list of order with product category = \"Baby\"")
	public void exercise2() {
//		long startTime = System.currentTimeMillis();
//		List<Order> result = orderRepo.findAll()
//				.stream()
//				.filter(o ->
//					o.getProducts()
//					.stream()
//					.anyMatch(p -> p.getCategory().equalsIgnoreCase("Baby"))
//				)
//				.collect(Collectors.toList());
//
//		long endTime = System.currentTimeMillis();
//
//		log.info(String.format("exercise 2 - execution time: %1$d ms", (endTime - startTime)));
//		result.forEach(o -> log.info(o.toString()));

		List<Order> res = orderRepo.findAll()
				.stream()
				.filter(order->order.getProducts()
						.stream()
						.anyMatch(product->product.getCategory().equals("Baby")))
				.toList();
		res.forEach(order-> log.info(order.toString()));
	}

	@Test
	@DisplayName("Obtain a list of product with category = “Toys” and then apply 10% discount\"")
	public void exercise3() {
//		long startTime = System.currentTimeMillis();
//
//		List<Product> result = productRepo.findAll()
//				.stream()
//				.filter(p -> p.getCategory().equalsIgnoreCase("Toys"))
//				.map(p -> p.withPrice(p.getPrice() * 0.9))
//				.collect(Collectors.toList());
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 3 - execution time: %1$d ms", (endTime - startTime)));
//		result.forEach(o -> log.info(o.toString()));

		List<Product> res = productRepo.findAll()
				.stream()
				.filter(product->product.getCategory().equals("Toys"))
				.map(product->product.withPrice(product.getPrice()*0.9))
				.toList();
		res.forEach(product-> log.info(product.toString()));
	}

	@Test
	@DisplayName("Obtain a list of products ordered by customer of tier 2 between 01-Feb-2021 and 01-Apr-2021")
	public void exercise4() {
//		long startTime = System.currentTimeMillis();
//		List<Product> result = orderRepo.findAll()
//		.stream()
//		.filter(o -> o.getCustomer().getTier() == 2)
//		.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
//		.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 4, 1)) <= 0)
//		.flatMap(o -> o.getProducts().stream())
//		.distinct()
//		.collect(Collectors.toList());
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 4 - execution time: %1$d ms", (endTime - startTime)));
//		result.forEach(o -> log.info(o.toString()));

		List<Product> res = orderRepo.findAll()
				.stream()
				.filter(order->order.getCustomer().getTier() == 2)
				.filter(order->order.getOrderDate().isAfter(LocalDate.of(2021,2,1)))
				.filter(order->order.getOrderDate().isBefore(LocalDate.of(2021,4,1)))
				.flatMap(order->order.getProducts().stream())
				.distinct()
				.toList();
		res.forEach(order-> log.info(order.toString()));
	}

	@Test
	@DisplayName("Get the 3 cheapest products of \"Books\" category")
	public void exercise5() {
//		long startTime = System.currentTimeMillis();
////              Optional<Product> result = productRepo.findAll()
////                              .stream()
////                              .filter(p -> p.getCategory().equalsIgnoreCase("Books"))
////                              .sorted(Comparator.comparing(Product::getPrice))
////                              .findFirst();
//
//		Optional<Product> result = productRepo.findAll()
//				.stream()
//				.filter(p -> p.getCategory().equalsIgnoreCase("Books"))
//				.min(Comparator.comparing(Product::getPrice));
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 5 - execution time: %1$d ms", (endTime - startTime)));
//		log.info(result.get().toString());

		List<Product> res = productRepo.findAll()
				.stream()
				.filter(product->product.getCategory().equals("Books"))
				.sorted(Comparator.comparing(Product::getPrice))
				.limit(3)
				.toList();
		res.forEach(product-> log.info(product.toString()));
	}

	@Test
	@DisplayName("Get the 3 most recent placed order")
	public void exercise6() {
//		long startTime = System.currentTimeMillis();
//		List<Order> result = orderRepo.findAll()
//				.stream()
//				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
//				.limit(3)
//				.collect(Collectors.toList());
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 6 - execution time: %1$d ms", (endTime - startTime)));
//		result.forEach(o -> log.info(o.toString()));

		List<Order> res = orderRepo.findAll()
				.stream()
				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.limit(3)
				.toList();
		res.forEach(order-> log.info(order.toString()));
	}

	@Test
	@DisplayName("Get a list of products which was ordered on 15-Mar-2021")
	public void exercise7() {
//		long startTime = System.currentTimeMillis();
//		List<Product> result = orderRepo.findAll()
//				.stream()
//				.filter(o -> o.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
//				.peek(o -> System.out.println(o.toString()))
//				.flatMap(o -> o.getProducts().stream())
//				.distinct()
//				.collect(Collectors.toList());
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 7 - execution time: %1$d ms", (endTime - startTime)));
//		result.forEach(o -> log.info(o.toString()));

		List<Product> res = orderRepo.findAll()
				.stream()
				.filter(order->order.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
				.flatMap(order->order.getProducts().stream())
				.distinct()
				.toList();
		res.forEach(product-> log.info(product.toString()));
	}

	@Test
	@DisplayName("Calculate the total lump of all orders placed in Feb 2021")
	public void exercise8() {
//		long startTime = System.currentTimeMillis();
//		double result = orderRepo.findAll()
//				.stream()
//				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
//				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 3, 1)) < 0)
//				.flatMap(o -> o.getProducts().stream())
//				.mapToDouble(Product::getPrice)
//				.sum();
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 8 - execution time: %1$d ms", (endTime - startTime)));
//		log.info("Total lump sum = " + result);

		double res = orderRepo.findAll()
				.stream()
				.filter(order->order.getOrderDate().isAfter(LocalDate.of(2021,2,1)))
				.filter(order->order.getOrderDate().isBefore(LocalDate.of(2021,3,1)))
				.flatMap(order->order.getProducts().stream())
				.mapToDouble(Product::getPrice)
				.sum();
		log.info("sum: " + res);
	}

	@Test
	@DisplayName("Calculate the total lump of all orders placed in Feb 2021 (using reduce with BiFunction)")
	public void exercise8a() {
//		BiFunction<Double, Product, Double> accumulator = (acc, product) -> acc + product.getPrice();
//
//		long startTime = System.currentTimeMillis();
//		double result = orderRepo.findAll()
//				.stream()
//				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 2, 1)) >= 0)
//				.filter(o -> o.getOrderDate().compareTo(LocalDate.of(2021, 3, 1)) < 0)
//				.flatMap(o -> o.getProducts().stream())
//				.reduce(0D, accumulator, Double::sum);
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 8a - execution time: %1$d ms", (endTime - startTime)));
//		log.info("Total lump sum = " + result);

		BiFunction<Double, Product, Double> total = (sum, product) -> sum + product.getPrice();

		double res = orderRepo.findAll()
				.stream()
				.filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021,2,1)))
				.filter(order -> order.getOrderDate().isBefore(LocalDate.of(2021,3,1)))
				.flatMap(order->order.getProducts().stream())
				.reduce(0.0, total, Double::sum);
		log.info("sum: " + res);
	}

	@Test
	@DisplayName("Calculate the average price of all orders placed on 15-Mar-2021")
	public void exercise9() {
//		long startTime = System.currentTimeMillis();
//		double result = orderRepo.findAll()
//				.stream()
//				.filter(o -> o.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
//				.flatMap(o -> o.getProducts().stream())
//				.mapToDouble(Product::getPrice)
//				.average().getAsDouble();
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 9 - execution time: %1$d ms", (endTime - startTime)));
//		log.info("Average = " + result);

		double res = orderRepo.findAll()
				.stream()
				.filter(order->order.getOrderDate().isEqual(LocalDate.of(2021,3,15)))
				.flatMap(order->order.getProducts().stream())
				.mapToDouble(Product::getPrice)
				.average().getAsDouble();
		log.info("avg: " + res);
	}

	@Test
	@DisplayName("Obtain statistics summary of all products belong to \"Books\" category")
	public void exercise10() {
//		long startTime = System.currentTimeMillis();
//		DoubleSummaryStatistics statistics = productRepo.findAll()
//				.stream()
//				.filter(p -> p.getCategory().equalsIgnoreCase("Books"))
//				.mapToDouble(Product::getPrice)
//				.summaryStatistics();
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 10 - execution time: %1$d ms", (endTime - startTime)));
//		log.info(String.format("count = %1$d, average = %2$f, max = %3$f, min = %4$f, sum = %5$f",
//				statistics.getCount(), statistics.getAverage(), statistics.getMax(), statistics.getMin(), statistics.getSum()));

		DoubleSummaryStatistics res = productRepo.findAll()
				.stream()
				.filter(product->product.getCategory().equals("Books"))
				.mapToDouble(Product::getPrice)
				.summaryStatistics();
		log.info(String.format("count = %1$d, average = %2$f, max = %3$f, min = %4$f, sum = %5$f",
				res.getCount(), res.getAverage(), res.getMax(), res.getMin(), res.getSum()));
	}

	@Test
	@DisplayName("Obtain a mapping of order id and the order's product count")
	public void exercise11() {
//		long startTime = System.currentTimeMillis();
//		Map<Long, Integer>  result = orderRepo.findAll()
//				.stream()
//				.collect(
//						Collectors.toMap(
//								Order::getId,
//								order -> order.getProducts().size())
//						);
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 11 - execution time: %1$d ms", (endTime - startTime)));
//		log.info(result.toString());

		Map<Long, Integer> res = orderRepo.findAll()
				.stream()
				.collect(Collectors.toMap(order -> order.getId(), order->order.getProducts().size()));
		log.info(res.toString());
	}

	@Test
	@DisplayName("Obtain a data map of customer and list of orders")
	public void exercise12() {
//		long startTime = System.currentTimeMillis();
//		Map<Customer, List<Order>> result = orderRepo.findAll()
//				.stream()
//				.collect(Collectors.groupingBy(Order::getCustomer));
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 12 - execution time: %1$d ms", (endTime - startTime)));
//		log.info(result.toString());

		Map<Customer, List<Order>> res = orderRepo.findAll()
				.stream()
				.collect(Collectors.groupingBy(Order::getCustomer));
		log.info(res.toString());
	}

	@Test
	@DisplayName("Obtain a data map of customer_id and list of order_id(s)")
	public void exercise12a() {
//		long startTime = System.currentTimeMillis();
//		HashMap<Long, List<Long>> result = orderRepo.findAll()
//				.stream()
//				.collect(
//						Collectors.groupingBy(
//								order -> order.getCustomer().getId(),
//								HashMap::new,
//								Collectors.mapping(Order::getId, Collectors.toList())));
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 12a - execution time: %1$d ms", (endTime - startTime)));
//		log.info(result.toString());

		Map<Long, List<Long>> res = orderRepo.findAll()
				.stream()
				.collect(Collectors.groupingBy(
								order -> order.getCustomer().getId(),
								Collectors.mapping(Order::getId, Collectors.toList())));
		log.info(res.toString());
	}

	@Test
	@DisplayName("Obtain a data map with order and its total price")
	public void exercise13() {
//		long startTime = System.currentTimeMillis();
//		Map<Order, Double> result = orderRepo.findAll()
//				.stream()
//				.collect(
//					Collectors.toMap(
//							Function.identity(),
//							order -> order.getProducts().stream()
//										.mapToDouble(Product::getPrice).sum())
//					);
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 13 - execution time: %1$d ms", (endTime - startTime)));
//		log.info(result.toString());

		Map<Order, Double> res = orderRepo.findAll()
				.stream()
				.collect(Collectors.toMap(
						order->order,
						order->order.getProducts().stream().mapToDouble(Product::getPrice).sum()
				));
		log.info(res.toString());
	}

	@Test
	@DisplayName("Obtain a data map with order and its total price (using reduce)")
	public void exercise13a() {
//		long startTime = System.currentTimeMillis();
//		Map<Long, Double> result = orderRepo.findAll()
//				.stream()
//				.collect(
//						Collectors.toMap(
//								Order::getId,
//								order -> order.getProducts().stream()
//										.reduce(0D, (acc, product) -> acc + product.getPrice(), Double::sum)
//						));
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 13a - execution time: %1$d ms", (endTime - startTime)));
//		log.info(result.toString());

		BiFunction<Double, Product, Double> total = (sum, product) -> sum + product.getPrice();
		Map<Long, Double> res = orderRepo.findAll()
				.stream()
				.collect(Collectors.toMap(
						order->order.getId(),
						order->order.getProducts()
								.stream()
								.reduce(0.0, total, Double::sum)
				));
		log.info(res.toString());
	}

	@Test
	@DisplayName("Obtain a data map of product name by category")
	public void exercise14() {
//		long startTime = System.currentTimeMillis();
//		Map<String, List<String>> result = productRepo.findAll()
//				.stream()
//				.collect(
//						Collectors.groupingBy(
//								Product::getCategory,
//								Collectors.mapping(Product::getName, Collectors.toList()))
//						);
//
//
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 14 - execution time: %1$d ms", (endTime - startTime)));
//		log.info(result.toString());

		Map<String, List<String>> res = productRepo.findAll()
				.stream()
				.collect(Collectors.groupingBy(
						Product::getCategory,
						Collectors.mapping(Product::getName, Collectors.toList())
				));
		log.info(res.toString());
	}

	@Test
	@DisplayName("Get the most expensive product per category")
	void exercise15() {
//		long startTime = System.currentTimeMillis();
//		Map<String, Optional<Product>> result = productRepo.findAll()
//				.stream()
//				.collect(
//						Collectors.groupingBy(
//								Product::getCategory,
//								Collectors.maxBy(Comparator.comparing(Product::getPrice)))
//						);
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 15 - execution time: %1$d ms", (endTime - startTime)));
//		log.info(result.toString());
////		result.forEach((k,v) -> {
////			log.info("key=" + k + ", value=" + v.get());
////		});

		Map<String, Optional<Product>> res = productRepo.findAll()
				.stream()
				.collect(
						Collectors.groupingBy(
								Product::getCategory,
								Collectors.maxBy(Comparator.comparing(Product::getPrice))
						)
				);
		log.info(res.toString());
	}
	
	@Test
	@DisplayName("Get the most expensive product (by name) per category")
	void exercise15a() {
		long startTime = System.currentTimeMillis();
		Map<String, String> result = productRepo.findAll()
				.stream()
				.collect(
						Collectors.groupingBy(
								Product::getCategory,
								Collectors.collectingAndThen(
										Collectors.maxBy(Comparator.comparingDouble(Product::getPrice)),
										optionalProduct -> optionalProduct.map(Product::getName).orElse(null)
								)
						));
//		long endTime = System.currentTimeMillis();
//		log.info(String.format("exercise 15a - execution time: %1$d ms", (endTime - startTime)));
//		log.info(result.toString());

		Map<String, String> res = productRepo.findAll()
				.stream()
				.collect(
						Collectors.groupingBy(
								Product::getCategory,
								Collectors.collectingAndThen(
										Collectors.maxBy(Comparator.comparing(Product::getPrice)),
										optionalProduct -> optionalProduct.map(Product::getName).orElse(null)
								)
						)
				);
		log.info(res.toString());
	}
}
