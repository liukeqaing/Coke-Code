package com.atguigu.tingshu.album.api;

// 导入需要的类和注解
import com.atguigu.tingshu.album.service.AlbumInfoService;
import com.atguigu.tingshu.common.result.Result;
import com.atguigu.tingshu.query.album.AlbumInfoQuery;
import com.atguigu.tingshu.vo.album.AlbumInfoVo;
import com.atguigu.tingshu.vo.album.TrackInfoVo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 使用 @Tag 注解为 API 接口添加描述，表示该接口属于 "专辑管理" 模块
 */
@Tag(name = "专辑管理")
/**
 * 使用 @RestController 注解将该类标记为一个 Spring MVC 控制器
 * 它会自动将方法的返回值转换为 JSON 或 XML 格式，并返回给客户端
 */
@RestController
/**
 * 使用 @RequestMapping 注解定义请求的基本路径
 * 所有在此类中定义的请求路径都将以 "/api/album/albumInfo" 开头
 */
@RequestMapping("/api/album/albumInfo")
/**
 * @SuppressWarnings 注解用于抑制编译器警告
 * 在这里，它被用来抑制关于泛型类型转换的警告
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class AlbumInfoApiController {
	/**
	 * 使用 @Autowired 注解自动注入 AlbumInfoService 实例
	 * AlbumInfoService 是一个服务类，负责处理与专辑信息相关的业务逻辑
	 */
	@Autowired
	private AlbumInfoService albumInfoService;
	/**
	 * 新增专辑信息的接口
	 * @param albumInfoVo 接收来自客户端的专辑信息数据，封装在 AlbumInfoVo 对象中
	 * @return 返回操作结果，使用 Result.ok() 方法表示操作成功
	 */
	@PostMapping(value = "/saveAlbumInfo")
	public Result saveAlbumInfo(@RequestBody AlbumInfoVo albumInfoVo) {
		// 调用 AlbumInfoService 的 saveAlbumInfo 方法，将专辑信息保存到数据库中
		albumInfoService.saveAlbumInfo(albumInfoVo);

		// 返回一个表示成功的 Result 对象，供客户端使用
		return Result.ok();
	}

	/* *
	 * 分页条件查询专辑的列表
	 * @param page
	 * @param size
	 * @param albumInfoQuery
	 * @return
	 */
	@PostMapping(value = "/findUserAlbumPage/{page}/{size}")
	public Result findUserAlbumPage(@PathVariable(value = "page") Long page,
									@PathVariable(value = "size") Long size,
									@RequestBody AlbumInfoQuery albumInfoQuery){

		return Result.ok(albumInfoService.findUserAlbumPage(page,size,albumInfoQuery));
	}

	/**
	 * 删除专辑信息
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/removeAlbumInfo/{id}")
	public Result removeAlbumInfo(@PathVariable(value = "id") Long id){
		albumInfoService.removeAlbumInfo(id);
		return Result.ok();
	}

	/**
	 * 专辑修改数据回显
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/getAlbumInfo/{id}")
	public Result getAlbumInfo(@PathVariable(value = "id")Long id){
		return Result.ok(albumInfoService.getAlbumInfo(id));
	}

	/**
	 * 修改专辑信息
	 * @param albumInfoVo
	 * @param id
	 * @return
	 */
	@PutMapping(value = "/updateAlbumInfo/{id}")
	public Result updateAlbumInfo(@RequestBody AlbumInfoVo albumInfoVo
			, @PathVariable(value = "id") Long id){
		albumInfoService.updateAlbumInfo(albumInfoVo,id);
		return Result.ok();
	}

	/**
	 * 声音新增时查询用户专辑列表
	 */
	@GetMapping(value = "/findUserAllAlbumList")
	public Result findUserAllAlbumList(){
		albumInfoService.findUserAllAlbumList();
		return Result.ok();
	}


}





















