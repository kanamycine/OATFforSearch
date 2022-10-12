package com.team6.onandthefarm.service.sns;

import com.team6.onandthefarm.dto.sns.*;
import com.team6.onandthefarm.entity.order.OrderProduct;
import com.team6.onandthefarm.entity.order.Orders;
import com.team6.onandthefarm.entity.product.Product;
import com.team6.onandthefarm.entity.product.Wish;
import com.team6.onandthefarm.entity.seller.Seller;
import com.team6.onandthefarm.entity.sns.*;
import com.team6.onandthefarm.entity.user.Following;
import com.team6.onandthefarm.entity.user.User;
import com.team6.onandthefarm.repository.order.OrderProductRepository;
import com.team6.onandthefarm.repository.order.OrderRepository;
import com.team6.onandthefarm.repository.product.ProductRepository;
import com.team6.onandthefarm.repository.product.ProductWishRepository;
import com.team6.onandthefarm.repository.seller.SellerRepository;
import com.team6.onandthefarm.repository.sns.*;
import com.team6.onandthefarm.repository.user.FollowingRepository;
import com.team6.onandthefarm.repository.user.UserRepository;
import com.team6.onandthefarm.util.DateUtils;
import com.team6.onandthefarm.vo.sns.feed.AddableProductResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedDetailResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedResponse;
import com.team6.onandthefarm.vo.sns.feed.imageProduct.ImageInfo;
import com.team6.onandthefarm.vo.sns.feed.imageProduct.ImageProductInfo;
import com.team6.onandthefarm.vo.sns.profile.ProfileMainFeedResponse;
import com.team6.onandthefarm.vo.sns.profile.ProfileMainScrapResponse;
import com.team6.onandthefarm.vo.sns.profile.ProfileMainWishResponse;
import com.team6.onandthefarm.vo.sns.profile.WishProductListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedServiceImpl implements FeedService {

	private final int pageContentNumber = 8;

	private final FeedRepository feedRepository;

//	private final MemberServiceClient memberServiceClient;
//
//	private final ProductServiceClient productServiceClient;
//
//	private final OrderServiceClient orderServiceClient;

	private final UserRepository userRepository;

	private final SellerRepository sellerRepository;

	private final FollowingRepository followingRepository;

	private final ProductRepository productRepository;

	private final ProductWishRepository productWishRepository;

	private final OrderRepository orderRepository;

	private final OrderProductRepository orderProductRepository;

	private final FeedImageRepository feedImageRepository;

	private final FeedLikeRepository feedLikeRepository;

	private final FeedTagRepository feedTagRepository;

	private final ScrapRepository scrapRepository;

	private final FeedImageProductRepository feedImageProductRepository;

	private DateUtils dateUtils;
	Environment env;


	@Autowired
	public FeedServiceImpl(FeedRepository feedRepository,
						   FeedImageRepository feedImageRepository,
						   FeedLikeRepository feedLikeRepository,
						   ScrapRepository scrapRepository,
						   FeedImageProductRepository feedImageProductRepository,
						   FeedTagRepository feedTagRepository,
						   UserRepository userRepository,
						   SellerRepository sellerRepository,
						   FollowingRepository followingRepository,
						   ProductRepository productRepository,
						   ProductWishRepository productWishRepository,
						   OrderRepository orderRepository,
						   OrderProductRepository orderProductRepository,
						   DateUtils dateUtils,
						   Environment env) {

		this.feedLikeRepository = feedLikeRepository;
		this.scrapRepository = scrapRepository;
		this.feedRepository = feedRepository;
		this.feedImageRepository = feedImageRepository;
		this.feedImageProductRepository = feedImageProductRepository;
		this.feedTagRepository = feedTagRepository;
		this.userRepository = userRepository;
		this.sellerRepository = sellerRepository;
		this.followingRepository = followingRepository;
		this.productRepository = productRepository;
		this.productWishRepository = productWishRepository;
		this.orderRepository = orderRepository;
		this.orderProductRepository = orderProductRepository;
		this.dateUtils = dateUtils;
		this.env = env;
	}

	/**
	 * feed 메인페이지 최신순 조회
	 * @param feedDto
	 * @return
	 */
	public List<FeedResponse> findByRecentFeedList(FeedDto feedDto) {
		List<Feed> feedList = new ArrayList<>();
		feedRepository.findAll().forEach(feedList::add);
		feedList.sort((o1, o2) -> {
			int result = o2.getFeedCreateAt().compareTo(o1.getFeedCreateAt());
			return result;
		});

		int startIndex = feedDto.getPageNumber() * pageContentNumber;

		int size = feedList.size();

		return getResponses(size, startIndex, feedList);
	}

	/**
	 * feed 메인페이지 좋아요순 조회
	 * @param pageNumber
	 * @return
	 */
	public List<FeedResponse> findByLikeFeedList(Integer pageNumber) {
		List<Feed> feedList = feedRepository.findAll(Sort.by(Sort.Direction.DESC, "feedLikeCount"));

		int startIndex = pageNumber * pageContentNumber;

		int size = feedList.size();

		return getResponses(size, startIndex, feedList);
	}

	/**
	 * feed 메인페이지 팔로우 조회
	 * @param memberId
	 * @param pageNumber
	 * @return
	 */
	public List<FeedResponse> findByFollowFeedList(Long memberId, Integer pageNumber) {
		List<Feed> feedList = new ArrayList<>();

		List<Following> followers = followingRepository.findByFollowingMemberId(memberId);

		for (Following follower : followers) {
			Feed feed = feedRepository.findByMemberId(follower.getFollowerMemberId());
			feedList.add(feed);
		}

		int startIndex = pageNumber * pageContentNumber;

		int size = feedList.size();

		return getResponses(size, startIndex, feedList);
	}

	/**
	 * feed 메인페이지 조회수순 조회
	 * @param pageNumber
	 * @return
	 */
	public List<FeedResponse> findByViewCountFeedList(Integer pageNumber) {
		List<Feed> feedList = feedRepository.findAll(Sort.by(Sort.Direction.DESC, "feedViewCount"));

		int startIndex = pageNumber * pageContentNumber;

		int size = feedList.size();

		return getResponses(size, startIndex, feedList);
	}

	/**
	 * 조회 목록을 페이징처리 해주는 메서드
	 * @param size
	 * @param startIndex
	 * @param feedList
	 * @return
	 */
	public List<FeedResponse> getResponses(int size, int startIndex, List<Feed> feedList) {
		List<FeedResponse> responseList = new ArrayList<>();
		if (size < startIndex + pageContentNumber) {
			for (Feed feed : feedList.subList(startIndex, size)) {

				FeedResponse response = FeedResponse.builder()
						.feedTitle(feed.getFeedTitle())
						.feedId(feed.getFeedId())
						.feedCommentCount(feed.getFeedCommentCount())
						.feedLikeCount(feed.getFeedLikeCount())
						.feedScrapCount(feed.getFeedScrapCount())
						.feedShareCount(feed.getFeedShareCount())
						.feedViewCount(feed.getFeedViewCount())
						.memberId(feed.getMemberId())
						.memberRole(Integer.valueOf(feed.getMemberRole()))
            			.feedContent(feed.getFeedContent())
						.build();

				if (feed.getMemberRole().equals("user")) { // 유저
					Optional<User> user = userRepository.findById(feed.getMemberId());
					response.setMemberName(user.get().getUserName());
				} else if (feed.getMemberRole().equals("seller")) { // 셀러
					Optional<Seller> seller = sellerRepository.findById(feed.getMemberId());
					response.setMemberName(seller.get().getSellerName());
				}
				List<FeedImage> feedImage = feedImageRepository.findByFeed(feed);
				response.setFeedImageSrc(feedImage.get(0).getFeedImageSrc());
				
				responseList.add(response);
			}
			return responseList;
		}
		for (Feed feed : feedList.subList(startIndex, startIndex + pageContentNumber)) {
			FeedResponse response = FeedResponse.builder()
					.feedTitle(feed.getFeedTitle())
					.feedId(feed.getFeedId())
					.feedCommentCount(feed.getFeedCommentCount())
					.feedLikeCount(feed.getFeedLikeCount())
					.feedScrapCount(feed.getFeedScrapCount())
					.feedShareCount(feed.getFeedShareCount())
					.feedViewCount(feed.getFeedViewCount())
					.memberId(feed.getMemberId())
					.memberRole(Integer.valueOf(feed.getMemberRole()))
          .feedContent(feed.getFeedContent())
					.build();

			if (feed.getMemberRole().equals("user")) { // 유저
				Optional<User> user = userRepository.findById(feed.getMemberId());
				response.setMemberName(user.get().getUserName());
			} else if (feed.getMemberRole().equals("seller")) { // 셀러
				Optional<Seller> seller = sellerRepository.findById(feed.getMemberId());
				response.setMemberName(seller.get().getSellerName());
			}
			List<FeedImage> feedImage = feedImageRepository.findByFeed(feed);
			response.setFeedImageSrc(feedImage.get(0).getFeedImageSrc());
			responseList.add(response);
		}
		return responseList;
	}

	/**
	 * 피드 좋아요 메서드
	 * @param feedId
	 * @param userId
	 * @return
	 */
	public Boolean createFeedLike(Long feedId, Long userId) {
		Optional<Feed> feed = feedRepository.findById(feedId);
		feed.get().setFeedLikeCount(feed.get().getFeedLikeCount() + 1);
		FeedLike feedLike = FeedLike.builder()
				.feed(feed.get())
				.memberId(userId)
				.build();
		FeedLike result = feedLikeRepository.save(feedLike);
		if (result == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public Boolean createFeedScrap(Long feedId, Long userId) {
		Optional<Feed> feed = feedRepository.findById(feedId);
		feed.get().setFeedScrapCount(feed.get().getFeedScrapCount() + 1);
		Scrap scrap = Scrap.builder()
				.feed(feed.get())
				.memberId(userId)
				.build();
		Scrap result = scrapRepository.save(scrap);
		if (result == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * 피드 업로드하는 메서드
	 * @param memberId, memberRole, feedInfoDto
	 * @return feedId
	 */
	@Override
	public Long uploadFeed(Long memberId, String memberRole, FeedInfoDto feedInfoDto) {

		Feed feed = new Feed();
		feed.setMemberId(memberId);
		feed.setMemberRole(memberRole);
		feed.setFeedTitle(feedInfoDto.getFeedTitle());
		feed.setFeedContent(feedInfoDto.getFeedContent());
		feed.setFeedViewCount(0);
		feed.setFeedLikeCount(0);
		feed.setFeedShareCount(0);
		feed.setFeedScrapCount(0);
		feed.setFeedCommentCount(0);
		feed.setFeedStatus(true);
		feed.setFeedCreateAt(dateUtils.transDate(env.getProperty("dateutils.format")));

		//피드 추가
		Feed savedFeed = feedRepository.save(feed);

		//피드 태그 추가
		for (String tag : feedInfoDto.getFeedTag()) {
			FeedTag feedTag = new FeedTag();
			feedTag.setFeed(savedFeed);
			feedTag.setFeedTagName(tag);
			feedTagRepository.save(feedTag);
		}

		int imageIndex = 0;
		for (MultipartFile imageSrc : feedInfoDto.getFeedImgSrcList()) {
			//imageSrc 처리 코드 필요
			String imageOriginName = imageSrc.getOriginalFilename();

			FeedImage feedImage = new FeedImage();
			feedImage.setFeed(savedFeed);
			feedImage.setFeedImageSrc(imageOriginName);

			//피드 이미지 추가
			FeedImage saveFeedImage = feedImageRepository.save(feedImage);

			for (ImageProductInfo imageProduct : feedInfoDto.getFeedProductIdList()) {
				if (imageProduct.getImageIndex() == imageIndex) {
					FeedImageProduct feedImageProduct = new FeedImageProduct();
					feedImageProduct.setFeedImage(saveFeedImage);
					feedImageProduct.setProductId(imageProduct.getProductId());

					//피드 이미지 별 상품 추가
					FeedImageProduct savedFeedImageProduct = feedImageProductRepository.save(feedImageProduct);
				}
			}
			imageIndex++;
		}

		return savedFeed.getFeedId();
	}

	/**
	 * 피드 업로드할 때 추가 가능한 상품을 조회하는 메서드
	 *
	 * @param memberId, memberRole
	 * @return List<AddableProductResponse>
	 */
	@Override
	public List<AddableProductResponse> findAddableProducts(Long memberId, String memberRole) {

		List<AddableProductResponse> addableProductList = new ArrayList<>();

		if(memberRole.equals("user")){
			List<Orders> ordersList = orderRepository.findWithOrderAndOrdersStatus(memberId);
			for(Orders orders : ordersList){
				//orderproduct받아와서 해당 productID로 조회하기
				List<OrderProduct> orderProductList = orderProductRepository.findByOrders(orders);
				for(OrderProduct orderProduct : orderProductList){
					Optional<Product> savedProduct = productRepository.findById(orderProduct.getProductId());
					Product product = savedProduct.get();

					Optional<Seller> savedSeller = sellerRepository.findById(product.getSeller().getSellerId());

					AddableProductResponse addableProductResponse = AddableProductResponse.builder()
							.productId(product.getProductId())
							.productName(product.getProductName())
							.productPrice(product.getProductPrice())
							.productMainImgSrc(product.getProductMainImgSrc())
							.sellerShopName(savedSeller.get().getSellerShopName())
							.build();

					addableProductList.add(addableProductResponse);
				}
			}
		}
		else if(memberRole.equals("seller")){
			Optional<Seller> savedSeller = sellerRepository.findById(memberId);
			Seller seller = savedSeller.get();

			List<Product> productList = productRepository.findBySeller(seller);

			for(Product product : productList) {
				AddableProductResponse addableProductResponse = AddableProductResponse.builder()
						.productId(product.getProductId())
						.productName(product.getProductName())
						.productPrice(product.getProductPrice())
						.productMainImgSrc(product.getProductMainImgSrc())
						.sellerShopName(savedSeller.get().getSellerShopName())
						.build();

				addableProductList.add(addableProductResponse);
			}
		}

		return addableProductList;
	}

	/**
	 * 피드 상세 페이지 조회하는 메서드
	 * @param feedId
	 * @return FeedDetailResponse
	 */
	@Override
	public FeedDetailResponse findFeedDetail(Long feedId, Long memberId) {

		FeedDetailResponse feedDetailResponse = null;

		Optional<Feed> savedFeed = feedRepository.findByIdAndStatus(feedId);
		if (savedFeed.isPresent()) {
			Feed feedEntity = savedFeed.get();

			List<ImageInfo> imageInfoList = new ArrayList<>();
			List<ImageProductInfo> imageProductInfoList = new ArrayList<>();

			List<FeedImage> savedFeedImageList = feedImageRepository.findByFeed(feedEntity);
			for (FeedImage feedImage : savedFeedImageList) {
				ImageInfo imageInfo = ImageInfo.builder()
						.feedImageId(feedImage.getFeedImageId())
						.feedImageSrc(feedImage.getFeedImageSrc())
						.build();
				imageInfoList.add(imageInfo);

				List<FeedImageProduct> savedFeedImageProductList = feedImageProductRepository.findByFeedImage(
						feedImage);
				for (FeedImageProduct feedImageProduct : savedFeedImageProductList) {
					ImageProductInfo imageProductInfo = ImageProductInfo.builder()
							.feedImageId(feedImage.getFeedImageId())
							.productId(feedImageProduct.getProductId())
							.build();
					imageProductInfoList.add(imageProductInfo);
				}
			}

			List<FeedTag> feedTagList = feedTagRepository.findByFeed(feedEntity);

			feedDetailResponse = FeedDetailResponse.builder()
					.feedId(feedEntity.getFeedId())
					.feedTitle(feedEntity.getFeedTitle())
					.feedContent(feedEntity.getFeedContent())
					.feedViewCount(feedEntity.getFeedViewCount())
					.feedLikeCount(feedEntity.getFeedLikeCount())
					.feedShareCount(feedEntity.getFeedShareCount())
					.feedScrapCount(feedEntity.getFeedScrapCount())
					.feedCommentCount(feedEntity.getFeedCommentCount())
					.feedCreateAt(feedEntity.getFeedCreateAt())
					.feedUpdateAt(feedEntity.getFeedUpdateAt())
					.feedImageList(imageInfoList)
					.feedImageProductList(imageProductInfoList)
					.feedTag(feedTagList)
					.build();

			if(savedFeed.get().getMemberId() == memberId){
				feedDetailResponse.setIsModifiable(true);
			}
		}

		return feedDetailResponse;
	}

	/**
	 * 피드 조회수 증가하는 메서드
	 * @param feedId
	 * @return Boolean
	 */
	@Override
	public Boolean upViewCount(Long feedId) {
		Optional<Feed> savedFeed = feedRepository.findById(feedId);
		if (savedFeed.isPresent()) {
			savedFeed.get().setFeedViewCount(savedFeed.get().getFeedViewCount() + 1);
			return true;
		}
		return false;
	}

	@Override
	public Boolean upShareCount(Long feedId) {
		Optional<Feed> savedFeed = feedRepository.findById(feedId);
		if (savedFeed.isPresent()) {
			savedFeed.get().setFeedShareCount(savedFeed.get().getFeedShareCount() + 1);
			return true;
		}
		return false;
	}

	@Override
	public List<ProfileMainFeedResponse> findByMemberFeedList(ProfileMainFeedDto profileMainFeedDto) {
		Long memberId = profileMainFeedDto.getMemberId();

		List<Feed> feedList = feedRepository.findFeedListByMemberId(memberId);
		List<ProfileMainFeedResponse> responseList = new ArrayList<>();
		for (Feed feed : feedList) {
			FeedImage feedThumbnailImage = feedImageRepository.findByFeed(feed).get(0);
			ProfileMainFeedResponse profileMainFeedResponse = ProfileMainFeedResponse.builder()
					.feedId(feed.getFeedId())
					.feedImg(feedThumbnailImage)
					.build();
			responseList.add(profileMainFeedResponse);
		}
		return responseList;
	}

	@Override
	public List<ProfileMainScrapResponse> findByMemberScrapList(ProfileMainScrapDto profileMainScrapDto) {
		Long memberId = profileMainScrapDto.getMemberId();
		List<ProfileMainScrapResponse> responseList = new ArrayList<>();
		List<Scrap> scrapList = scrapRepository.findScrapListByMemberId(memberId);
		for (Scrap scrap : scrapList) {
			ProfileMainScrapResponse profileMainScrapResponse = ProfileMainScrapResponse.builder()
					.feedId(scrap.getFeed().getFeedId())
					.feedImg(feedImageRepository.findByFeed(scrap.getFeed()).get(0))
					.build();

			Optional<Feed> savedFeed = feedRepository.findById(scrap.getFeed().getFeedId());
			if(savedFeed.get().getMemberRole().equals("user")) {
				Optional<User> user = userRepository.findById(savedFeed.get().getMemberId());
				profileMainScrapResponse.setMemberName(user.get().getUserName());
			}
			else{
				Optional<Seller> seller = sellerRepository.findById(savedFeed.get().getMemberId());
				profileMainScrapResponse.setMemberName(seller.get().getSellerName());
			}
			responseList.add(profileMainScrapResponse);
		}
		return responseList;
	}

	@Override
	public List<ProfileMainWishResponse> findByMemberWishList(ProfileMainWishDto profileMainWishDto) {
		Long memberId = profileMainWishDto.getMemberId();
		List<ProfileMainWishResponse> responseList = new ArrayList<>();

		List<Wish> wishList = productWishRepository.findWishListByUserId(memberId);
		for(Wish wish : wishList){
			Optional<Product> product = productRepository.findById(wish.getProduct().getProductId());
			ProfileMainWishResponse profileMainWishResponse = ProfileMainWishResponse.builder()
					.productId(product.get().getProductId())
					.productImgSrc(product.get().getProductMainImgSrc())
					.build();
			responseList.add(profileMainWishResponse);
		}

		return responseList;
	}

	public List<FeedResponse> findByRecentFeedListAndMemberId(ProfileFeedDto profileFeedDto) {
		List<Feed> feedList = feedRepository.findFeedListByMemberId(profileFeedDto.getMemberId());

		int startIndex = profileFeedDto.getPageNumber() * pageContentNumber;

		int size = feedList.size();

		return getResponses(size, startIndex, feedList);
	}
	public List<FeedResponse> findByRecentScrapFeedListAndMemberId(ProfileFeedDto profileFeedDto) {

		List<Scrap> scrapList = scrapRepository.findScrapListByMemberId(profileFeedDto.getMemberId());
		List<Feed> feedList = new ArrayList<>();
		for (Scrap scrap : scrapList) {
			feedList.add(scrap.getFeed());
		}

		int startIndex = profileFeedDto.getPageNumber() * pageContentNumber;

		int size = feedList.size();

		return getResponses(size, startIndex, feedList);
	}

	@Override
	public List<WishProductListResponse> findByMemberWishDetailList(ProfileMainWishDto profileMainWishDto) {
		Long memberId = profileMainWishDto.getMemberId();
		List<WishProductListResponse> wishProductListResponses = new ArrayList<>();

		List<Wish> wishList = productWishRepository.findWishListByUserId(memberId);
		for(Wish wish : wishList){
			WishProductListResponse wishProductListResponse = WishProductListResponse.builder()
					.productId(wish.getProduct().getProductId())
					.productName(wish.getProduct().getProductName())
					.productPrice(wish.getProduct().getProductPrice())
					.productMainImgSrc(wish.getProduct().getProductMainImgSrc())
					.productOriginPlace(wish.getProduct().getProductOriginPlace())
					.productWishCount(wish.getProduct().getProductWishCount())
					.productStatus(wish.getProduct().getProductStatus())
					.build();

			wishProductListResponses.add(wishProductListResponse);
		}

		return wishProductListResponses;
	}
}
