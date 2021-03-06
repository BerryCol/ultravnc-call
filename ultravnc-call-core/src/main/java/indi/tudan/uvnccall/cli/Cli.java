package indi.tudan.uvnccall.cli;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import indi.tudan.uvnccall.common.ConfigConstants;
import indi.tudan.uvnccall.utils.VNCUtils;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * VNCServer 启动命令行接口
 *
 * @author wangtan
 * @date 2021-01-18 14:51:25
 * @since 1.0
 */
public class Cli {

    /**
     * 启动 vnc server
     *
     * @param args 命令行参数
     * @return boolean
     * @author wangtan
     * @date 2021-02-07 16:38:20
     * @since 1.0.0
     */
    public static boolean startServer(String... args) {

        // 创建一个解析器
        CommandLineParser parser = new DefaultParser();

        // 创建一个 Options，用来包装 option
        Options options = new Options();

        // help
        options.addOption("h", "help", false, "Print help");

        // 编号，唯一，必须是数字，且至少三位（即最小是 100，最大是 2147483647）
        options.addOption(Option.builder("id")
                .longOpt("id")
                .hasArg()
                .desc("编号，唯一，必须是数字，且至少三位（即最小是 100，最大是 2147483647）")
                .build()
        );

        // winvnc.exe 路径（可选参数）
        options.addOption(Option.builder("dir")
                .longOpt("directory")
                .hasArg()
                .desc("winvnc.exe 路径（可选参数）")
                .build()
        );

        // 中继器服务器 IP（可选参数）
        options.addOption(Option.builder("ip")
                .longOpt("ip")
                .hasArg()
                .desc("中继器服务器 IP（可选参数）")
                .build()
        );

        // 中继器 UltraVNC Server 监听端口（可选参数）
        options.addOption(Option.builder("p")
                .longOpt("port")
                .hasArg()
                .desc("中继器 UltraVNC Server 监听端口（可选参数）")
                .build()
        );

        String id = "";
        String directory = "";
        String ip = "";
        String port = "";

        try {

            // 解析命令行参数
            CommandLine line = parser.parse(options, args);

            // 使用帮助
            if (line.hasOption('h')) {

                HelpFormatter hf = new HelpFormatter();
                hf.setWidth(120);
                // 打印使用帮助
                hf.printHelp("ultravnc-call", options, true);

                return false;

            }

            // 编号，唯一，必须是数字，且至少三位（即最小是 100，最大是 2147483647）
            if (line.hasOption("id")) {
                id = line.getOptionValue("id");
            }

            // winvnc.exe 路径（可选参数）
            if (line.hasOption("dir")) {
                directory = line.getOptionValue("dir");
            }

            // 中继器服务器 IP（可选参数）
            if (line.hasOption("ip")) {
                ip = line.getOptionValue("ip");
            }

            // 中继器 UltraVNC Server 监听端口（可选参数）
            if (line.hasOption("p")) {
                port = line.getOptionValue("p");
            }

            if (StrUtil.isBlank(id)) {
                StaticLog.error(ConfigConstants.START_ULTRAVNC_SERVER_NO_PARAMETER_INFO);
                return false;
            }
            if (StrUtil.isAllBlank(directory, ip, port)) {
                VNCUtils.startUltraVNCServer(id);
            } else if (StrUtil.isNotBlank(directory)
                    && StrUtil.isAllBlank(ip, port)) {
                VNCUtils.startUltraVNCServer(id, directory);
            } else if (StrUtil.isNotBlank(directory)
                    && StrUtil.isNotBlank(ip)
                    && StrUtil.isBlank(port)) {
                VNCUtils.startUltraVNCServer(id, directory, ip);
            } else if (StrUtil.isAllNotBlank(id, directory, ip, port)) {
                VNCUtils.startUltraVNCServer(id, directory, ip, port);
            } else {
                StaticLog.error(ConfigConstants.START_ULTRAVNC_SERVER_NO_PARAMETER_INFO);
                return false;
            }

            return true;

        } catch (ParseException e) {
            StaticLog.info("Unexpected exception:" + e.getMessage());
        }

        return false;

    }

    /**
     * 停止 vnc server
     *
     * @param args 命令行参数
     * @return boolean
     * @author wangtan
     * @date 2021-02-07 16:39:10
     * @since 1.0.0
     */
    public static boolean stopServer(String... args) {

        // 创建一个解析器
        CommandLineParser parser = new DefaultParser();

        // 创建一个 Options，用来包装 option
        Options options = new Options();

        // help
        options.addOption("h", "help", false, "Print help");

        // vncviewer.exe 路径（可选参数）
        options.addOption(Option.builder("i")
                .longOpt("image")
                .hasArg()
                .desc("winvnc.exe 镜像名称（可选参数）")
                .build()
        );

        String image = "";

        try {

            // 解析命令行参数
            CommandLine line = parser.parse(options, args);

            // 使用帮助
            if (line.hasOption('h')) {

                HelpFormatter hf = new HelpFormatter();
                hf.setWidth(120);
                // 打印使用帮助
                hf.printHelp("ultravnc-call", options, true);

                return false;

            }

            // vncviewer.exe 路径（可选参数）
            if (line.hasOption("i")) {
                image = line.getOptionValue("i");
            }

            if (StrUtil.isBlank(image)) {
                VNCUtils.stopUltraVNCServerByDefaultImageName();
            } else {
                VNCUtils.stopUltraVNCServer(args[0]);
            }

            return true;

        } catch (ParseException e) {
            StaticLog.info("Unexpected exception:" + e.getMessage());
        }

        return false;

    }

    /**
     * 启动 vnc viewer
     *
     * @param args 命令行参数
     * @return boolean
     * @author wangtan
     * @date 2021-02-07 16:39:34
     * @since 1.0.0
     */
    public static boolean startViewer(String... args) {

        // 创建一个解析器
        CommandLineParser parser = new DefaultParser();

        // 创建一个 Options，用来包装 option
        Options options = new Options();

        // help
        options.addOption("h", "help", false, "Print help");

        // 编号，唯一，必须是数字，且至少三位（即最小是 100，最大是 2147483647）
        options.addOption(Option.builder("id")
                .longOpt("id")
                .hasArg()
                .desc("编号，唯一，必须是数字，且至少三位（即最小是 100，最大是 2147483647）")
                .build()
        );

        // vncviewer.exe 路径（可选参数）
        options.addOption(Option.builder("dir")
                .longOpt("directory")
                .hasArg()
                .desc("vncviewer.exe 路径（可选参数）")
                .build()
        );

        // 中继器服务器 IP（可选参数）
        options.addOption(Option.builder("ip")
                .longOpt("ip")
                .hasArg()
                .desc("中继器服务器 IP（可选参数）")
                .build()
        );

        // 中继器 UltraVNC Viewer 监听端口（可选参数）
        options.addOption(Option.builder("p")
                .longOpt("port")
                .hasArg()
                .desc("中继器 UltraVNC Viewer 监听端口（可选参数）")
                .build()
        );

        String id = "";
        String directory = "";
        String ip = "";
        String port = "";

        try {

            // 解析命令行参数
            CommandLine line = parser.parse(options, args);

            // 使用帮助
            if (line.hasOption('h')) {

                HelpFormatter hf = new HelpFormatter();
                hf.setWidth(120);
                // 打印使用帮助
                hf.printHelp("ultravnc-call", options, true);

                return false;

            }

            // 编号，唯一，必须是数字，且至少三位（即最小是 100，最大是 2147483647）
            if (line.hasOption("id")) {
                id = line.getOptionValue("id");
            }

            // vncviewer.exe 路径（可选参数）
            if (line.hasOption("dir")) {
                directory = line.getOptionValue("dir");
            }

            // 中继器服务器 IP（可选参数）
            if (line.hasOption("ip")) {
                ip = line.getOptionValue("ip");
            }

            // 中继器 UltraVNC Viewer 监听端口（可选参数）
            if (line.hasOption("p")) {
                port = line.getOptionValue("p");
            }

            if (StrUtil.isBlank(id)) {
                StaticLog.error(ConfigConstants.START_ULTRAVNC_SERVER_NO_PARAMETER_INFO);
                return false;
            }
            if (StrUtil.isAllBlank(directory, ip, port)) {
                VNCUtils.startUltraVNCViewer(id);
            } else if (StrUtil.isNotBlank(directory)
                    && StrUtil.isAllBlank(ip, port)) {
                VNCUtils.startUltraVNCViewer(id, directory);
            } else if (StrUtil.isNotBlank(directory)
                    && StrUtil.isNotBlank(ip)
                    && StrUtil.isBlank(port)) {
                VNCUtils.startUltraVNCViewer(id, directory, ip);
            } else if (StrUtil.isAllNotBlank(id, directory, ip, port)) {
                VNCUtils.startUltraVNCViewer(id, directory, ip, port);
            } else {
                StaticLog.error(ConfigConstants.START_ULTRAVNC_VIEWER_NO_PARAMETER_INFO);
                return false;
            }

            return true;

        } catch (ParseException e) {
            StaticLog.info("Unexpected exception:" + e.getMessage());
        }

        return false;

    }

    /**
     * 修改 vnc ultravnc.ini 属性文件
     *
     * @param args 命令行参数
     * @return boolean
     * @author wangtan
     * @date 2021-02-08 10:27:12
     * @since 1.0.0
     */
    public static boolean modifierIni(String... args) {

        // 创建一个解析器
        CommandLineParser parser = new DefaultParser();

        // 创建一个 Options，用来包装 option
        Options options = new Options();

        // help
        options.addOption("h", "help", false, "Print help");

        // 编号，唯一，必须是数字，且至少三位（即最小是 100，最大是 2147483647）
        options.addOption(Option.builder("id")
                .longOpt("id")
                .hasArg()
                .desc("编号，唯一，必须是数字，且至少三位（即最小是 100，最大是 2147483647）")
                .build()
        );

        // vncviewer.exe 路径（可选参数）
        options.addOption(Option.builder("dir")
                .longOpt("directory")
                .hasArg()
                .desc("ultravnc.ini 配置文件路径（可选参数）")
                .build()
        );

        String id = "";
        String directory = "";

        try {

            // 解析命令行参数
            CommandLine line = parser.parse(options, args);

            // 使用帮助
            if (line.hasOption('h')) {

                HelpFormatter hf = new HelpFormatter();
                hf.setWidth(120);
                // 打印使用帮助
                hf.printHelp("ultravnc-call", options, true);

                return false;

            }

            // 编号，唯一，必须是数字，且至少三位（即最小是 100，最大是 2147483647）
            if (line.hasOption("id")) {
                id = line.getOptionValue("id");
            }

            // ultravnc.ini 配置文件路径（可选参数）
            if (line.hasOption("dir")) {
                directory = line.getOptionValue("dir");
            }

            if (StrUtil.isBlank(id)) {
                StaticLog.error(ConfigConstants.MODIFY_ULTRAVNC_INI_NO_PARAMETER_INFO);
                return false;
            }
            if (StrUtil.isBlank(directory)) {
                VNCUtils.modifyUltraVNCIni(id);
            } else if (StrUtil.isNotBlank(directory)) {
                VNCUtils.startUltraVNCViewer(id, directory);
            } else {
                StaticLog.error(ConfigConstants.START_ULTRAVNC_INI_MODIFIER_ERROR_INFO);
                return false;
            }

            return true;

        } catch (ParseException e) {
            StaticLog.info("Unexpected exception:" + e.getMessage());
        }

        return false;

    }

}
