package com.zhglxt.common.util.random;

import com.zhglxt.common.util.uuid.IdUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Description 随机姓名生成工具类
 * @Author liuwy
 * @Date 2020/8/13
 */

public class RandomNameUtil {
    //生成随机数
    private static Random random = new Random(System.currentTimeMillis());
    /* 598 百家姓 */
    private static String[] familyName = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
            "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
            "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐", "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷",
            "罗", "毕", "郝", "邬", "安", "常", "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄", "和",
            "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧", "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒",
            "屈", "项", "祝", "董", "梁", "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危", "江", "童", "颜", "郭", "梅", "盛", "林", "刁", "钟",
            "徐", "邱", "骆", "高", "夏", "蔡", "田", "樊", "胡", "凌", "霍", "虞", "万", "支", "柯", "昝", "管", "卢", "莫", "经", "房", "裘", "缪", "干", "解", "应",
            "宗", "丁", "宣", "贲", "邓", "郁", "单", "杭", "洪", "包", "诸", "左", "石", "崔", "吉", "钮", "龚", "程", "嵇", "邢", "滑", "裴", "陆", "荣", "翁", "荀",
            "羊", "于", "惠", "甄", "曲", "家", "封", "芮", "羿", "储", "靳", "汲", "邴", "糜", "松", "井", "段", "富", "巫", "乌", "焦", "巴", "弓", "牧", "隗", "山",
            "谷", "车", "侯", "宓", "蓬", "全", "郗", "班", "仰", "秋", "仲", "伊", "宫", "宁", "仇", "栾", "暴", "甘", "钭", "厉", "戎", "祖", "武", "符", "刘", "景",
            "詹", "束", "龙", "叶", "幸", "司", "韶", "郜", "黎", "蓟", "溥", "印", "宿", "白", "怀", "蒲", "邰", "从", "鄂", "索", "咸", "籍", "赖", "卓", "蔺", "屠",
            "蒙", "池", "乔", "阴", "郁", "胥", "能", "苍", "双", "闻", "莘", "党", "翟", "谭", "贡", "劳", "逄", "姬", "申", "扶", "堵", "冉", "宰", "郦", "雍", "却",
            "璩", "桑", "桂", "濮", "牛", "寿", "通", "边", "扈", "燕", "冀", "浦", "尚", "农", "温", "别", "庄", "晏", "柴", "瞿", "阎", "充", "慕", "连", "茹", "习",
            "宦", "艾", "鱼", "容", "向", "古", "易", "慎", "戈", "廖", "庾", "终", "暨", "居", "衡", "步", "都", "耿", "满", "弘", "匡", "国", "文", "寇", "广", "禄",
            "阙", "东", "欧", "殳", "沃", "利", "蔚", "越", "夔", "隆", "师", "巩", "厍", "聂", "晁", "勾", "敖", "融", "冷", "訾", "辛", "阚", "那", "简", "饶", "空",
            "曾", "毋", "沙", "乜", "养", "鞠", "须", "丰", "巢", "关", "蒯", "相", "查", "后", "荆", "红", "游", "郏", "竺", "权", "逯", "盖", "益", "桓", "公", "仉",
            "督", "岳", "帅", "缑", "亢", "况", "郈", "有", "琴", "归", "海", "晋", "楚", "闫", "法", "汝", "鄢", "涂", "钦", "商", "牟", "佘", "佴", "伯", "赏", "墨",
            "哈", "谯", "篁", "年", "爱", "阳", "佟", "言", "福", "南", "火", "铁", "迟", "漆", "官", "冼", "真", "展", "繁", "檀", "祭", "密", "敬", "揭", "舜", "楼",
            "疏", "冒", "浑", "挚", "胶", "随", "高", "皋", "原", "种", "练", "弥", "仓", "眭", "蹇", "覃", "阿", "门", "恽", "来", "綦", "召", "仪", "风", "介", "巨",
            "木", "京", "狐", "郇", "虎", "枚", "抗", "达", "杞", "苌", "折", "麦", "庆", "过", "竹", "端", "鲜", "皇", "亓", "老", "是", "秘", "畅", "邝", "还", "宾",
            "闾", "辜", "纵", "侴", "万俟", "司马", "上官", "欧阳", "夏侯", "诸葛", "闻人", "东方", "赫连", "皇甫", "羊舌", "尉迟", "公羊", "澹台", "公冶", "宗正",
            "濮阳", "淳于", "单于", "太叔", "申屠", "公孙", "仲孙", "轩辕", "令狐", "钟离", "宇文", "长孙", "慕容", "鲜于", "闾丘", "司徒", "司空", "兀官", "司寇",
            "南门", "呼延", "子车", "颛孙", "端木", "巫马", "公西", "漆雕", "车正", "壤驷", "公良", "拓跋", "夹谷", "宰父", "谷梁", "段干", "百里", "东郭", "微生",
            "梁丘", "左丘", "东门", "西门", "南宫", "第五", "公仪", "公乘", "太史", "仲长", "叔孙", "屈突", "尔朱", "东乡", "相里", "胡母", "司城", "张廖", "雍门",
            "毋丘", "贺兰", "綦毋", "屋庐", "独孤", "南郭", "北宫", "王孙"};

    /**
     * 随机获取单个中文
     *
     * @return
     */
    public static String getChinese() {
        String str = null;
        int highPos, lowPos;
        Random random = new Random();
        highPos = (176 + Math.abs(random.nextInt(39)));//区码，0xA0打头，从第16区开始，即0xB0=11*16=176,16~55一级汉字，56~87二级汉字
        random = new Random();
        lowPos = 161 + Math.abs(random.nextInt(93));//位码，0xA0打头，范围第1~94列

        byte[] bArr = new byte[2];
        bArr[0] = (new Integer(highPos)).byteValue();
        bArr[1] = (new Integer(lowPos)).byteValue();
        try {
            str = new String(bArr, "GBK");//区位码组合成汉字
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String girl = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";

    static List<String> dassler = Arrays.asList(familyName);

    /**
     * 随机生成2~4个字的名字
     *
     * @return
     */
    public static String getChineseName() {

        int index = random.nextInt(familyName.length - 1);
        String xing = familyName[index];

        /*姓*/
        String name = xing; //获得一个随机的姓氏

        /*名*/
        //第二个字
        String tow = getChinese();
        //第三个字
        String three = getChinese();

        /*
        从常用字中选取一个或两个字作为名
        nextBoolean：从该随机数生成器的序列得到下一个伪均匀分布的布尔值
        */
        if (random.nextBoolean()) {
            if (dassler.contains(tow)) {
                //第二个字如果包含 姓氏，重新生成
                tow = getChinese();
            }
            if (dassler.contains(three)) {
                //第三个字如果包含姓氏，重新生成
                three = getChinese();
            }
            //拼接名字
            name += tow + three;

        } else {
            if (dassler.contains(tow)) {
                //如果第二个字包含姓，重新生成
                tow = getChinese();
            }
            name += tow;
        }
        return name;
    }

    /**
     * 根据指定的"姓"，随机生成2~4个字的名字
     *
     * @param xing
     * @return
     */
    public static String getChineseName(String xing) {

        /*姓*/
        String name = xing;

        /*名*/
        //第二个字
        String tow = getChinese();
        //第三个字
        String three = getChinese();

        /*
        从常用字中选取一个或两个字作为名
        nextBoolean：从该随机数生成器的序列得到下一个伪均匀分布的布尔值
        */
        if (random.nextBoolean()) {
            if (dassler.contains(tow)) {
                //第二个字如果包含 姓氏，重新生成
                tow = getChinese();
            }
            if (dassler.contains(three)) {
                //第三个字如果包含姓氏，重新生成
                three = getChinese();
            }
            name += tow + three;
        } else {
            if (dassler.contains(tow)) {
                //第二个字如果包含 姓氏，重新生成
                tow = getChinese();
            }
            name += tow;
        }
        return name;
    }

    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    /**
     * 随机生成手机号码
     *
     * @return
     */
    public static String getPhoneNumber() {
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }


    public static String base = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String[] email_suffix = "@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.net,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn".split(",");

    /**
     * 随机生成邮箱地址
     *
     * @param lMin 最小长度
     * @param lMax 最大长度
     * @return
     */
    public static String getEmail(int lMin, int lMax) {
        int length = getNum(lMin, lMax);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = (int) (Math.random() * base.length());
            sb.append(base.charAt(number));
        }
        sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);

        char c = sb.charAt(0);
        if (c == '0') {
            //以0开头的邮箱地址，把0去掉
            sb.replace(0, 1, "");
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        /*
        System.out.println("\r\n随机生成2~4个字的名字");
        for (int i = 0; i < 100; i++) {
            System.out.print(getChineseName()+ "\t");
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }

        System.out.println("\r\n根据指定的\"姓\"，随机生成2~4个字的名字");
        for (int i = 0; i < 100; i++) {
            System.out.print(getChineseName("刘")+ "\t");
            if ((i + 1) % 10 == 0) {
                System.out.println();
            }
        }
        */

        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {

            String name = getChineseName();

            /* 模拟数据 */
            Map map = new HashMap();
            map.put("id", IdUtils.fastSimpleUUID());
            map.put("name", name);

            // 0：女  1：男
            String gender = "1";//默认：男
            for (int j = 1; j < name.length(); j++) {
                char c = name.charAt(j);
                int index = girl.indexOf(c);
                if (index != -1) {
                    gender = "0";//女
                }
            }
            map.put("gender", gender);

            map.put("phoneNumber", getPhoneNumber());
            map.put("email", getEmail(5, 10));
            map.put("remark", "用户测试数据");
            list.add(map);
        }

        System.out.println("用户：" + list.toString());
    }
}
