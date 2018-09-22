package com.big.data.test.test2_oodsPartition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class OddsRunner {
        

    public static void main(String[] args) throws Exception {

        args = new String[]{"api-test/hadoop-test/test2/in", "api-test/hadoop-test/test2/out"};
        
        // 1.获取配置信息,创建任务对象
        Configuration conf = new Configuration();
        // 提交到yarn上运行
        // conf.set("mapreduce.framework.name", "yarn"); //  linux -yarn- mod
        // conf.set("yarn.resourcemanager.hostname", "hadoop101"); //
        // win-yarn- mod
        
        Job job = Job.getInstance(conf);
        
        // 2.注册驱动类(可执行jar的入口)
        // job.setJar("f:/test/wc.jar");
        job.setJarByClass(OddsRunner.class);
        
        // 3.注册 Mapper ,Reducer类
        job.setMapperClass(WCMapper.class);
        job.setReducerClass(WCReducer.class);
        
        // 4.注册 Mapper 的输出K-V类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        // 注册环形缓冲区溢写分区类(必须注册reduce-task并发数,自定义分区才会生效)
        job.setPartitionerClass(OddsPartitioner.class);
        // 设置reduce-task并发数
        job.setNumReduceTasks(2);

        // 5.注册最终输出结果的K-V类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        // 6.注册文件输入,结果输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        // 7.提交运行
        boolean completion = job.waitForCompletion(true);
        System.exit(completion ? 0 : 1);
        
    }
        
}
         
