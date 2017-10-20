# 图片上传

## 获取ftp 服务器信息

ClientApiService.getConfigByKey(key)
key = "PRITURE_FTP_SERVER"
需要解码 javax.crypto 
``` 
csharp 实现 需要替换成java
﻿        public static string Decode(string data)
        {
            byte[] byKey = System.Text.ASCIIEncoding.ASCII.GetBytes(KEY_64);
            byte[] byIV = System.Text.ASCIIEncoding.ASCII.GetBytes(IV_64);

            byte[] byEnc;
            try
            {
                byEnc = Convert.FromBase64String(data);
            }
            catch
            {
                return null;
            }

            DESCryptoServiceProvider cryptoProvider = new DESCryptoServiceProvider();
            MemoryStream ms = new MemoryStream(byEnc);
            CryptoStream cst = new CryptoStream(ms, cryptoProvider.CreateDecryptor(byKey,

byIV), CryptoStreamMode.Read);
            StreamReader sr = new StreamReader(cst);
            return sr.ReadToEnd();
        }
```
解密后的字符串 空格分割 得到
```
      key = "PRITURE_FTP_SERVER"
﻿    string code = ClientApiService.getConfigByKey(key);
            //解密
     string decode = Security.Decode(code);
﻿            string[] strs = decode.Split(new[] { ' ' }, StringSplitOptions.RemoveEmptyEntries);

            UserName = strs[0];
            PSW = strs[1];//密码
            Host = strs[2];
            Port = strs[3];

            if (strs.Length == 5)
                WorkDir = strs[4]; 工作路径
```
## 获取上传路径

ftp 路径
```
picpath=﻿ClientApiService.getPicFtpPathByCommodityNo(commodityNo);
﻿int catIndex = picPath.LastIndexOf("/") + 1;
﻿string catStructName = picPath.Substring(catIndex); //
﻿ bool isCutThree = Util.isCutThreeByStructNameAndPicName(catStructName, pictrueName);//判断是否需要切三张小图
﻿ picPath = picPath.Remove(catIndex); //最终保存图片路径

﻿   public static bool isCutThreeByStructNameAndPicName(string catStructName, string pictrueName)
        {
            var reg1 = new System.Text.RegularExpressions.Regex(@"^[\d]{6,9}_0[126]_l.jpg$");
            if (!reg1.IsMatch(pictrueName))
                return false;
            if (pictrueName.EndsWith("02_l.jpg"))
            {
                if (catStructName.StartsWith("10"))//女鞋
                    return true;
                if (catStructName.StartsWith("12"))//男鞋
                    return true;
                if (catStructName.StartsWith("15-10"))//户外休闲-户外鞋
                    return true;
                if (catStructName.StartsWith("15-14"))//户外休闲-休闲鞋
                    return true;
                return false;
            }
            if (pictrueName.EndsWith("06_l.jpg"))
                return catStructName.StartsWith("11-11");//运动-运动鞋
            //是第一张
            if (catStructName.StartsWith("10"))//女鞋
                return false;
            if (catStructName.StartsWith("12"))//男鞋
                return false;
            if (catStructName.StartsWith("15-10"))//户外休闲-户外鞋
                return false;
            if (catStructName.StartsWith("15-14"))//户外休闲-休闲鞋
                return false;
            if (catStructName.StartsWith("11-11"))
                return false;
            return true;
        }
```
将图片上传

## 切图

filePath=picpath+pictureName;
 ClientApiService.catPic(filePath, isCutThree ? "1" : "0", "0"); // 路径  是否切3张小图,是否自动切宝贝描述默认为0

## 保存 图片信息
* 图片类型为l图 则 保存这么多

```
﻿ string[] PicTypes = new string[] { "m", "t", "mb", "ms", "ml", "sl" };
            foreach (string type in PicTypes)
            {
                string picName = pictrueName.Replace("l.jpg", type + ".jpg");
                if (commodityPictureVOs.FirstOrDefault(m => m.PicName.IndexOf(picName, System.StringComparison.Ordinal) != -1) == null)
                {
                    Picture commodityPictureM = CreateModel(picPath, commodityNo);//
                    commodityPictureM.PicName = picName;
                    commodityPictureM.PicType = type;
                    commodityPictures.Add(commodityPictureM);
                }
            }

```

* 如果 ﻿isCutThree 为 true
在增加 这三种图片

```

string[] PicThreeType = new string[] { "s", "c", "u" };
﻿            foreach (string type in PicThreeType)
            {
                string picName = commodityNo + "_01_" + type + ".jpg";
                if (commodityPictureVOs.FirstOrDefault(m => m.PicName.IndexOf(picName, System.StringComparison.Ordinal) != -1) == null)
                {
                    Picture commodityPictureM = CreateModel(picPath, commodityNo);//
                    commodityPictureM.PicName = picName;
                    commodityPictureM.PicType = type;
                    commodityPictures.Add(commodityPictureM);
                }
            }
```
﻿ * 为b图 
保存一条b图 信息即可      