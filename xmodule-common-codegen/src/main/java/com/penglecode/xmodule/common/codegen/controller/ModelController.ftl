package ${packageName};

<#list jdkImports as ipm>
import ${ipm};
</#list>

<#list thirdImports as ipm>
import ${ipm};
</#list>

<#list projectImports as ipm>
import ${ipm};
</#list>

/**
 * ${modelName}Controller
 * 
 * @author 	${author}
 * @date	${date}
 */
@RestController
public class ${controllerClassName} <#if extendsClassName??>extends ${extendsClassName} </#if>{

	@Resource(name="${serviceBeanName}")
	private ${serviceClassName} ${serviceBeanName};
	
	/**
	 * 根据条件查询${modelName}列表(分页、排序)
	 * @param condition		- 查询参数
	 * @param page			- 分页参数
	 * @param sort			- 排序参数
	 * @return
	 */
	@GetMapping(value="${prefixOfApiUrl}/${domainOfApiUrl}/list", produces=MediaType.APPLICATION_JSON_VALUE)
	public PageResult<List<${modelClassName}>> get${modelAliasName}ListByPage(${modelClassName} condition, Page page, Sort sort) {
		List<${modelClassName}> dataList = ${serviceBeanName}.get${modelAliasName}ListByPage(condition, page, sort);
		return PageResult.success().message("OK").data(dataList).totalRowCount(page.getTotalRowCount()).build();
	}
	
	/**
	 * 创建${modelName}
	 * @param parameter		- 创建参数
	 * @return
	 */
	@PostMapping(value="${prefixOfApiUrl}/${domainOfApiUrl}/create", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> create${modelAliasName}(@RequestBody ${modelClassName} parameter) {
		${serviceBeanName}.create${modelAliasName}(parameter);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 更新${modelName}
	 * @param parameter		- 更新参数
	 * @return
	 */
	@PutMapping(value="${prefixOfApiUrl}/${domainOfApiUrl}/update", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> update${modelAliasName}(@RequestBody ${modelClassName} parameter) {
		${serviceBeanName}.update${modelAliasName}(parameter);
		return Result.success().message("保存成功!").build();
	}
	
	/**
	 * 根据${modelName}ID获取${modelName}详情
	 * @param ${modelIdName}		- ${modelName}ID
	 * @return
	 */
	@GetMapping(value="${prefixOfApiUrl}/${domainOfApiUrl}/${modelIdPath}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<${modelClassName}> get${modelAliasName}ById(<#if !isCompositePrimaryKey>@PathVariable("${modelIdName}") </#if>${modelIdClassName} ${modelIdName}) {
		${modelClassName} ${modelReferenceName} = ${serviceBeanName}.get${modelAliasName}ById(${modelIdName});
		return Result.success().message("OK").data(${modelReferenceName}).build();
	}
	
	/**
	 * 根据${modelName}ID删除${modelName}
	 * @param ${modelIdName}		- ${modelName}ID
	 * @return
	 */
	@DeleteMapping(value="${prefixOfApiUrl}/${domainOfApiUrl}/${modelIdPath}", produces=MediaType.APPLICATION_JSON_VALUE)
	public Result<Object> delete${modelAliasName}ById(<#if !isCompositePrimaryKey>@PathVariable("${modelIdName}") </#if>${modelIdClassName} ${modelIdName}) {
		${serviceBeanName}.delete${modelAliasName}ById(${modelIdName});
		return Result.success().message("删除成功!").build();
	}
	
}