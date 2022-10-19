package com.team6.onandthefarm.controller.sns;

import com.team6.onandthefarm.dto.sns.ProfileFeedDto;
import com.team6.onandthefarm.dto.sns.ProfileMainFeedDto;
import com.team6.onandthefarm.dto.sns.ProfileMainScrapDto;
import com.team6.onandthefarm.dto.sns.ProfileMainWishDto;
import com.team6.onandthefarm.dto.user.MemberProfileDto;
import com.team6.onandthefarm.service.sns.FeedService;
import com.team6.onandthefarm.util.BaseResponse;
import com.team6.onandthefarm.vo.sns.feed.FeedResponse;
import com.team6.onandthefarm.vo.sns.profile.*;
import com.team6.onandthefarm.vo.user.MemberProfileCountResponse;
import com.team6.onandthefarm.vo.user.MemberProfileRequest;
import com.team6.onandthefarm.vo.user.MemberProfileResponse;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/sns")
public class ProfileController {
	private FeedService feedService;

	@Autowired
	public ProfileController(FeedService feedService){
		this.feedService = feedService;
	}

	@PostMapping ("/profile/main-feed")
	@ApiOperation(value = "프로필 메인 화면 feed 부분 조회")
	public ResponseEntity<BaseResponse<List<ProfileMainFeedResponse>>> getProfileMainFeed(@ApiIgnore Principal principal, @RequestBody ProfileFeedRequest profileFeedRequest){

		ProfileMainFeedDto profileMainFeedDto = new ProfileMainFeedDto();

		Long memberId = null;
		if(profileFeedRequest.getMemberId() == null){
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileMainFeedDto.setMemberId(memberId);
		}
		else {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			profileMainFeedDto = modelMapper.map(profileFeedRequest, ProfileMainFeedDto.class);
		}

		List<ProfileMainFeedResponse> feedList = feedService.findByMemberFeedList(profileMainFeedDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(feedList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@PostMapping("/profile/main-scrap")
	@ApiOperation(value = "프로필 메인 화면 scrap 부분 조회")
	public ResponseEntity<BaseResponse<List<ProfileMainScrapResponse>>> getProfileMainScrap(@ApiIgnore Principal principal, @RequestBody ProfileFeedRequest profileFeedRequest){

		ProfileMainScrapDto profileMainScrapDto = new ProfileMainScrapDto();

		Long memberId = null;
		if(profileFeedRequest.getMemberId() == null) {
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileMainScrapDto.setMemberId(memberId);
		}
		else {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			profileMainScrapDto = modelMapper.map(profileFeedRequest, ProfileMainScrapDto.class);
		}

		List<ProfileMainScrapResponse> scrapList = feedService.findByMemberScrapList(profileMainScrapDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(scrapList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@PostMapping("/profile/main-wish")
	@ApiOperation(value = "프로필 메인 화면 wish 부분 조회")
	public ResponseEntity<BaseResponse<List<ProfileMainWishResponse>>> getProfileMainWish(@ApiIgnore Principal principal, @RequestBody ProfileFeedRequest profileFeedRequest){

		ProfileMainWishDto profileMainWishDto = new ProfileMainWishDto();

		Long memberId = null;
		if(profileFeedRequest.getMemberId() == null) {
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileMainWishDto.setMemberId(memberId);
		}
		else {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			profileMainWishDto = modelMapper.map(profileFeedRequest, ProfileMainWishDto.class);
		}

		List<ProfileMainWishResponse> wishList = feedService.findByMemberWishList(profileMainWishDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(wishList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@PostMapping("/profile/feed")
	@ApiOperation(value = "프로필 피드 전체 조회")
	public ResponseEntity<BaseResponse<List<FeedResponse>>> getProfileFeedResponse(@ApiIgnore Principal principal, @RequestBody ProfileFeedRequest profileFeedRequest) {

		ProfileFeedDto profileFeedDto = new ProfileFeedDto();

		Long memberId = null;
		if(profileFeedRequest.getMemberId() == null) {
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileFeedDto.setMemberId(memberId);
		}
		else {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			profileFeedDto = modelMapper.map(profileFeedRequest, ProfileFeedDto.class);
		}

		List<FeedResponse> responses = feedService.findByRecentFeedListAndMemberId(profileFeedDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(responses)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@PostMapping("/profile/scrap")
	@ApiOperation(value = "프로필 스크랩 전체 조회")
	public ResponseEntity<BaseResponse<List<FeedResponse>>> getProfileScrapFeedResponse(@ApiIgnore Principal principal, @RequestBody ProfileFeedRequest profileFeedRequest) {

		ProfileFeedDto profileFeedDto = new ProfileFeedDto();

		Long memberId = null;
		if(profileFeedRequest.getMemberId() == null) {
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileFeedDto.setMemberId(memberId);
		}
		else {
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			profileFeedDto = modelMapper.map(profileFeedRequest, ProfileFeedDto.class);
		}

		List<FeedResponse> responses = feedService.findByRecentScrapFeedListAndMemberId(profileFeedDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(responses)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@PostMapping("/profile/wish")
	@ApiOperation(value = "프로필 메인 화면 wish 전체 조회")
	public ResponseEntity<BaseResponse<List<WishProductListResponse>>> getProfileWishDetailList(@ApiIgnore Principal principal, @RequestBody ProfileFeedRequest profileFeedRequest){

		ProfileMainWishDto profileMainWishDto = new ProfileMainWishDto();

		Long memberId = null;
		if(profileFeedRequest.getMemberId() == null) {
			String[] principalInfo = principal.getName().split(" ");
			memberId = Long.parseLong(principalInfo[0]);
			profileMainWishDto.setMemberId(memberId);
		}
		else{
			ModelMapper modelMapper = new ModelMapper();
			modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
			profileMainWishDto = modelMapper.map(profileFeedRequest, ProfileMainWishDto.class);
		}

		List<WishProductListResponse> wishList = feedService.findByMemberWishDetailList(profileMainWishDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(wishList)
				.build();

		return new ResponseEntity(response,HttpStatus.OK);
	}

	@PostMapping("/profile/count")
	@ApiOperation(value = "멤버의 스크랩 수, 좋아요 수 조회")
	public ResponseEntity<BaseResponse<MemberProfileCountResponse>> getScrapLikeCount(@ApiIgnore Principal principal,
																					  @RequestBody MemberProfileRequest memberProfileRequest){

		MemberProfileDto memberProfileDto = new MemberProfileDto();

		Long loginId = null;
		String loginRole = null;
		if(memberProfileRequest.getMemberId() == null) {
			String[] principalInfo = principal.getName().split(" ");
			loginId = Long.parseLong(principalInfo[0]);
			loginRole = principalInfo[1];
			memberProfileDto.setMemberId(loginId);
			memberProfileDto.setMemberRole(loginRole);
		}else{
			memberProfileDto.setMemberId(memberProfileRequest.getMemberId());
			memberProfileDto.setMemberRole(memberProfileRequest.getMemberRole());
		}

		MemberProfileCountResponse memberProfileCountResponse = feedService.getScrapLikeCount(memberProfileDto);

		BaseResponse response = BaseResponse.builder()
				.httpStatus(HttpStatus.OK)
				.message("OK")
				.data(memberProfileCountResponse)
				.build();

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
