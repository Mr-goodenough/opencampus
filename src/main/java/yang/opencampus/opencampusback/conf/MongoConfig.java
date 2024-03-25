package yang.opencampus.opencampusback.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;

@Configuration
public class MongoConfig {
    
    public MongoConfig(MappingMongoConverter mappingMongoConverter) {
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null)); // 禁用类型映射
    }
}
//为了解决_class的问题，这个字段虽然是mongodb生成的，但是会有一些配置上的问题- 