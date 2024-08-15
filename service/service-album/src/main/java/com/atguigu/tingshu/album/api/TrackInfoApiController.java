package com.atguigu.tingshu.album.api;

import com.atguigu.tingshu.album.service.TrackInfoService;
import com.atguigu.tingshu.common.result.Result;
import com.atguigu.tingshu.vo.album.TrackInfoVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "声音管理")
@RestController
@RequestMapping("/api/album/trackInfo")
@SuppressWarnings({"unchecked", "rawtypes"})
public class TrackInfoApiController {

	@Autowired
	private TrackInfoService trackInfoService;

	/**
	 * 声音上传
	 * @param file
	 * @return
	 * @RequestParam 常用于从 URL 请求中获取参数，例如在处理 GET 或 POST 请求时，将请求中的参数映射到方法的参数中
	 */
	@PostMapping(value = "/uploadTrack")
	public Result uploadTrack(@RequestParam("file") MultipartFile file){  //MultipartFile是Spring中用于表示上传文件的接口,通常用于处理文件上传请求。
		return Result.ok(trackInfoService.uploadTrack(file));
	}
	/**
	 * 保存声音信息
	 * @param trackInfoVo
	 * @return
	 */
	@PostMapping(value = "/saveTrackInfo")
	public Result saveTrackInfo(@RequestBody TrackInfoVo trackInfoVo){
		trackInfoService.saveTrackInfo(trackInfoVo);
		return Result.ok();
	}


}

