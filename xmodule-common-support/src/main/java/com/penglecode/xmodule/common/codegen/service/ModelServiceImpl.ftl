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

@Service("${serviceBeanName}")
public class ${serviceImplClassName} implements ${serviceClassName} {

	@Autowired
	private ${modelClassName}Mapper ${modelClassNameLower}Mapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void create${modelAliasName}(${modelClassName} parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		${modelClassNameLower}Mapper.insertModel(parameter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void update${modelAliasName}(${modelClassName} parameter) {
		ValidationAssert.notNull(parameter, "参数不能为空");
		Map<String, Object> paramMap = parameter.mapBuilder()
		<#list modelMapWithMethods as withMethod>
												.${withMethod}()
		</#list>
												.build();
		${modelClassNameLower}Mapper.updateModelById(parameter.${getModelIdMethodName}(), paramMap);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public void delete${modelAliasName}ById(${modelIdClassName} id) {
		ValidationAssert.notNull(id, "id不能为空");
		${modelClassNameLower}Mapper.deleteModelById(id);
	}

	@Override
	public ${modelClassName} get${modelAliasName}ById(${modelIdClassName} id) {
		return ModelDecodeUtils.decodeModel(${modelClassNameLower}Mapper.selectModelById(id));
	}

	@Override
	public List<${modelClassName}> get${modelAliasName}ListByPage(${modelClassName} condition, Page page, Sort sort) {
		List<${modelClassName}> dataList = ModelDecodeUtils.decodeModel(${modelClassNameLower}Mapper.selectModelPageListByExample(condition, sort, new RowBounds(page.getOffset(), page.getLimit())));
    	page.setTotalRowCount(${modelClassNameLower}Mapper.countModelPageListByExample(condition));
		return dataList;
	}

	@Override
	public List<${modelClassName}> getAll${modelAliasName}List() {
		return ModelDecodeUtils.decodeModel(${modelClassNameLower}Mapper.selectAllModelList());
	}

}