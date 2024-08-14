package com.atguigu.tingshu.album.api;

import com.atguigu.tingshu.album.service.BaseCategoryService;
import com.atguigu.tingshu.common.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "分类管理")
@RestController
@RequestMapping(value="/api/album/category")
@SuppressWarnings({"unchecked", "rawtypes"})
public class BaseCategoryApiController {
	
	@Autowired
	private BaseCategoryService baseCategoryService;

	/**
	 * 查询所有的分类信息
	 * @return
	 */
	@GetMapping(value = "/getBaseCategoryList")
	public Result getBaseCategoryList(){
		return Result.ok(baseCategoryService.getIndexCategory());
	}

	/**
	 * 查询标签测试
	 * @param id
	 * @return
	 * @PathVariable 是 Spring MVC 框架中的一个注解，用于将请求 URL 中的路径参数绑定到控制器处理方法的参数上。
	 * 	路径参数绑定：@PathVariable 用于从请求的 URL 路径中提取参数，并将其传递给处理该请求的控制器方法的参数。
	 * 	例如，如果 URL 包含动态部分（如 /users/{id}），@PathVariable 可以用来捕获这个动态部分的值，并将其作为方法参数。
	 *
	 */
	@GetMapping(value = "/findAttribute/{id}")
	public Result findAttribute(@PathVariable(value = "id") Long id){
		return Result.ok(baseCategoryService.findAttribute(id));

	}

}

