package com.saki9.sharding;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.stereotype.Component;

import com.saki9.utils.DateUtil;

@Component
public class VideoViewStrategy implements ComplexKeysShardingAlgorithm<Date> {
	
	@Override
	public Collection<String> doSharding(Collection<String> arg0, ComplexKeysShardingValue<Date> arg1) {
		Collection<Date> collection = arg1.getColumnNameAndShardingValuesMap().get("pubdate");
		for (Date date : collection) {
			String year = DateUtil.dateToStr(date).split("-")[0];
			Integer month = Integer.parseInt(DateUtil.dateToStr(date).split("-")[1]);
			month = month % 3 == 0 ? month - 1 : month;
			for (String tableName : arg0) {
				if (year.equals(tableName.split("_")[2]) && month / 3 + 1 == Integer.parseInt(tableName.split("_")[3])) {
					return Arrays.asList(tableName);
				}
			}
		}
		return null;
	}

}
