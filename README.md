WeatherForcast
====
[天气预报](https://github.com/UMRhamster/WeatherForecast/blob/master/app/release/app-release.apk)
## 1.界面显示 ##
<table>
<tr>
<td>
<img src="https://github.com/UMRhamster/WeatherForecast/blob/master/app/screenshoot/Screenshot_2018-07-03-19-21-31-1538920185.png" width="320" hegiht="180" align=center />
</td>
<td>
<img src="https://github.com/UMRhamster/WeatherForecast/blob/master/app/screenshoot/Screenshot_2018-07-03-19-21-40-0071966823.png" width="320" hegiht="180" align=center />
</td>
</tr>
<tr>
<td>
<img src="https://github.com/UMRhamster/WeatherForecast/blob/master/app/screenshoot/Screenshot_2018-07-03-19-21-47-1789761352.png" width="320" hegiht="180" align=center />
</td>
<td>
<img src="https://github.com/UMRhamster/WeatherForecast/blob/master/app/screenshoot/Screenshot_2018-07-03-19-22-00-1212334712.png" width="320" hegiht="180" align=center />
</td>
</tr>
</table>

----

## 2.数据来源 ##
#### 天气数据：http://wthrcdn.etouch.cn/weather_mini?city=武汉 ####

JSON格式：

    {
    "data": {
        "yesterday": {
            "date": "2日星期一",
            "high": "高温 32℃",
            "fx": "南风",
            "low": "低温 25℃",
            "fl": "<![CDATA[<3级]]>",
            "type": "小雨"
        },
        "city": "武汉",
        "aqi": "44",
        "forecast": [
            {
                "date": "3日星期二",
                "high": "高温 32℃",
                "fengli": "<![CDATA[<3级]]>",
                "low": "低温 26℃",
                "fengxiang": "南风",
                "type": "小雨"
            },
            {
                "date": "4日星期三",
                "high": "高温 34℃",
                "fengli": "<![CDATA[3-4级]]>",
                "low": "低温 27℃",
                "fengxiang": "东南风",
                "type": "小雨"
            },
            {
                "date": "5日星期四",
                "high": "高温 31℃",
                "fengli": "<![CDATA[3-4级]]>",
                "low": "低温 24℃",
                "fengxiang": "西南风",
                "type": "中雨"
            },
            {
                "date": "6日星期五",
                "high": "高温 30℃",
                "fengli": "<![CDATA[<3级]]>",
                "low": "低温 24℃",
                "fengxiang": "西南风",
                "type": "中雨"
            },
            {
                "date": "7日星期六",
                "high": "高温 31℃",
                "fengli": "<![CDATA[3-4级]]>",
                "low": "低温 24℃",
                "fengxiang": "西北风",
                "type": "小雨"
            }
        ],
        "ganmao": "各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。",
        "wendu": "29"
        },
        "status": 1000,
        "desc": "OK"
        }

### 城市分为省市县三级 ###
#### 一级列表：  http://www.weather.com.cn/data/city3jdata/china.html ####

JSON格式：

    {
    "10101": "北京",
    "10102": "上海",
    "10103": "天津",
    "10104": "重庆",
    "10105": "黑龙江",
    "10106": "吉林",
    "10107": "辽宁",
    "10108": "内蒙古",
    "10109": "河北",
    "10110": "山西",
    "10111": "陕西",
    "10112": "山东",
    "10113": "新疆",
    "10114": "西藏",
    "10115": "青海",
    "10116": "甘肃",
    "10117": "宁夏",
    "10118": "河南",
    "10119": "江苏",
    "10120": "湖北",
    "10121": "浙江",
    "10122": "安徽",
    "10123": "福建",
    "10124": "江西",
    "10125": "湖南",
    "10126": "贵州",
    "10127": "四川",
    "10128": "广东",
    "10129": "云南",
    "10130": "广西",
    "10131": "海南",
    "10132": "香港",
    "10133": "澳门",
    "10134": "台湾"
    }

#### 二级列表：http://www.weather.com.cn/data/city3jdata/provshi/10120.html ####
以湖北为例

JSON格式：

    {
    "10": "恩施",
    "11": "十堰",
    "12": "神农架",
    "13": "随州",
    "14": "荆门",
    "15": "天门",
    "16": "仙桃",
    "17": "潜江",
    "01": "武汉",
    "02": "襄阳",
    "03": "鄂州",
    "04": "孝感",
    "05": "黄冈",
    "06": "黄石",
    "07": "咸宁",
    "08": "荆州",
    "09": "宜昌"
    }

#### 三级列表：http://www.weather.com.cn/data/city3jdata/station/1012001.html ####
以武汉为例

JSON格式：

    {
    "01": "武汉",
    "02": "蔡甸",
    "03": "黄陂",
    "04": "新洲",
    "05": "江夏",
    "06": "东西湖"
    }
----
## 3.使用到的技术 ##
### &emsp;百度定位 ###
#### &emsp;&emsp;&emsp;用于定位当前城市 ####
### &emsp;OKHttp 网络请求框架 ###
#### &emsp;&emsp;&emsp;用于进行网络请求，包括省市县数据和天气数据 ####
### &emsp;LitePal 数据库框架 ###
#### &emsp;&emsp;&emsp;用于对数据存储，包括省市县数据和天气数据 ####
### &emsp;MPAndroidChart 图表库 ###
#### &emsp;&emsp;&emsp;用于绘制最高温最低温的折线图 ####


