package com.team6.onandthefarm.service.exhibition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team6.onandthefarm.dto.exhibition.datatool.BadgeDataRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.BannerDataRequestDto;
import com.team6.onandthefarm.dto.exhibition.datatool.ProductDataRequestDto;
import com.team6.onandthefarm.entity.cart.Cart;
import com.team6.onandthefarm.entity.exhibition.item.Badge;
import com.team6.onandthefarm.entity.exhibition.item.Banner;
import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItem;
import com.team6.onandthefarm.entity.exhibition.item.ExhibitionItems;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.Wish;
import com.team6.onandthefarm.entity.review.Review;
import com.team6.onandthefarm.repository.cart.CartRepository;
import com.team6.onandthefarm.repository.exhibition.DataPickerRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionAccountRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionCategoryRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionItemRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionItemsRepository;
import com.team6.onandthefarm.repository.exhibition.ExhibitionRepository;
import com.team6.onandthefarm.repository.exhibition.item.BadgeRepository;
import com.team6.onandthefarm.repository.exhibition.item.BannerRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.product.ProductWishRepository;
import com.team6.onandthefarm.repository.review.ReviewRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.exhibition.datatool.BadgeATypeResponse;
import com.team6.onandthefarm.vo.exhibition.datatool.BadgeATypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerATypeResponse;
import com.team6.onandthefarm.vo.exhibition.datatool.BannerATypeResponses;
import com.team6.onandthefarm.vo.exhibition.datatool.ProductATypeResponse;
import com.team6.onandthefarm.vo.exhibition.datatool.ProductATypeResponses;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class DataToolServiceImpl implements DataToolService{
	private ExhibitionAccountRepository exhibitionAccountRepository;
	private ExhibitionCategoryRepository exhibitionCategoryRepository;
	private ExhibitionItemsRepository exhibitionItemsRepository;
	private ExhibitionItemRepository exhibitionItemRepository;
	private ExhibitionRepository exhibitionRepository;
	private DataPickerRepository dataPickerRepository;
	private ProductRepository productRepository;
	private ProductWishRepository productWishRepository;
	private CartRepository cartRepository;
	private ReviewRepository reviewRepository;
	private BannerRepository bannerRepository;
	private BadgeRepository badgeRepository;
	private DateUtils dateUtils;
	private Environment env;

	private ExhibitionItemComparator exhibitionItemComparator = new ExhibitionItemComparator();

	public DataToolServiceImpl(
			ExhibitionAccountRepository exhibitionAccountRepository,
			ExhibitionCategoryRepository exhibitionCategoryRepository,
			ExhibitionItemsRepository exhibitionItemsRepository,
			ExhibitionItemRepository exhibitionItemRepository,
			ExhibitionRepository exhibitionRepository,
			DataPickerRepository dataPickerRepository,
			BannerRepository bannerRepository,
			BadgeRepository badgeRepository,
			ProductRepository productRepository,
			ProductWishRepository productWishRepository,
			CartRepository cartRepository,
			ReviewRepository reviewRepository,
			DateUtils dateUtils, Environment env) {
		this.exhibitionAccountRepository = exhibitionAccountRepository;
		this.exhibitionCategoryRepository = exhibitionCategoryRepository;
		this.exhibitionItemsRepository = exhibitionItemsRepository;
		this.exhibitionItemRepository = exhibitionItemRepository;
		this.exhibitionRepository = exhibitionRepository;
		this.dataPickerRepository = dataPickerRepository;
		this.bannerRepository = bannerRepository;
		this.badgeRepository = badgeRepository;
		this.productRepository = productRepository;
		this.productWishRepository = productWishRepository;
		this.cartRepository = cartRepository;
		this.reviewRepository = reviewRepository;
		this.dateUtils = dateUtils;
		this.env = env;
	}

	@Override
	public ProductATypeResponses getProductATypeItems(ProductDataRequestDto productDataRequestDto, Long userId){
		ExhibitionItems exhibitionItems = exhibitionItemsRepository.findById(productDataRequestDto.getItemsId()).get();
		ProductATypeResponses productATypeResponsesResult = new ProductATypeResponses();
		List<ProductATypeResponse> productATypeResponses = new ArrayList<>();

		List<ExhibitionItem> items = exhibitionItemRepository.findExhibitionItemByExhibitionItemsId(exhibitionItems.getExhibitionItemsId());
		Collections.sort(items, exhibitionItemComparator);
		for (ExhibitionItem item : items) {
			Product product = productRepository.findById(item.getExhibitionItemNumber()).get();
			List<Review> reviews = reviewRepository.findReviewByProduct(product);

			ProductATypeResponse productATypeResponse = ProductATypeResponse.builder()
					.productId(item.getExhibitionItemId())
					.ImgSrc(product.getProductMainImgSrc())
					.productName(product.getProductName())
					.productPrice(product.getProductPrice())
					.sellerName(product.getSeller().getSellerName())
					.reviewRate(0.0)
					.reviewCount(reviews.size())
					.wishStatus(false)
					.cartStatus(false)
					.build();

			if(reviews.size() > 0) {
				Integer reviewSum = 0;
				for (Review review : reviews) {
					reviewSum += review.getReviewRate();
				}
				productATypeResponse.setReviewRate((double) reviewSum / reviews.size());
			}

			if(userId != null){
				Optional<Wish> savedWish = productWishRepository.findWishByUserAndProduct(userId, product.getProductId());
				if(savedWish.isPresent()){
					productATypeResponse.setWishStatus(true);
				}

				Optional<Cart> savedCart = cartRepository.findNotDeletedCartByProduct(product.getProductId(), userId);
				if(savedCart.isPresent()){
					productATypeResponse.setCartStatus(true);
				}
			}
			productATypeResponses.add(productATypeResponse);
		}
		productATypeResponsesResult.setProductATypeResponses(productATypeResponses);

		return productATypeResponsesResult;
	}

	@Override
	public BannerATypeResponses getBannerATypeItems(BannerDataRequestDto bannerDataRequestDto){
		ExhibitionItems exhibitionItems = exhibitionItemsRepository.findById(bannerDataRequestDto.getItemsId()).get();
		BannerATypeResponses bannerATypeResponsesResult = new BannerATypeResponses();
		List<BannerATypeResponse> bannerATypeResponses = new ArrayList<>();

		List<ExhibitionItem> items = exhibitionItemRepository.findExhibitionItemByExhibitionItemsId(exhibitionItems.getExhibitionItemsId());
		Collections.sort(items, exhibitionItemComparator);
		for (ExhibitionItem item : items) {
			Banner banner = bannerRepository.findById(item.getExhibitionItemNumber()).get();
			BannerATypeResponse bannerATypeResponse = BannerATypeResponse.builder()
					.ImgSrc(banner.getBannerImg())
					.connectUrl(banner.getBannerConnectUrl())
					.priority(item.getExhibitionItemPriority())
					.build();
			bannerATypeResponses.add(bannerATypeResponse);
		}
		bannerATypeResponsesResult.setBannerATypeResponses(bannerATypeResponses);

		return bannerATypeResponsesResult;
	}

	@Override
	public BadgeATypeResponses getBadgeATypeItems(BadgeDataRequestDto badgeDataRequestDto) {
		ExhibitionItems exhibitionItems = exhibitionItemsRepository.findById(badgeDataRequestDto.getItemsId()).get();
		BadgeATypeResponses badgeATypeResponsesResult = new BadgeATypeResponses();
		List<BadgeATypeResponse> BadgeATypeResponses = new ArrayList<>();

		List<ExhibitionItem> items = exhibitionItemRepository.findExhibitionItemByExhibitionItemsId(
				exhibitionItems.getExhibitionItemsId());
		Collections.sort(items, exhibitionItemComparator);

		for (ExhibitionItem item : items) {
			Badge badge = badgeRepository.findById(item.getExhibitionItemId()).get();
			BadgeATypeResponse badgeATypeResponse = BadgeATypeResponse.builder()
					.ImgSrc(badge.getBadgeImg())
					.connectUrl(badge.getBadgeConnectUrl())
					.badgeName(badge.getBadgeName())
					.priority(item.getExhibitionItemPriority())
					.build();
			BadgeATypeResponses.add(badgeATypeResponse);
		}
		badgeATypeResponsesResult.setBadgeATypeResponseList(BadgeATypeResponses);

		return badgeATypeResponsesResult;
	}

	class ExhibitionItemComparator implements Comparator<ExhibitionItem> {

		@Override
		public int compare(ExhibitionItem exhibitionItem1, ExhibitionItem exhibitionItem2) {
			Integer item1Priority = exhibitionItem1.getExhibitionItemPriority();
			Integer item2Priority = exhibitionItem2.getExhibitionItemPriority();

			if(item1Priority < item2Priority){
				return -1;
			}
			else if (item1Priority > item2Priority){
				return 1;
			}
			else {
				return 0;
			}

		}
	}


}
