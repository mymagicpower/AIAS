## 目录：
http://aias.top/

### 下载模型, 更新配置yml文件 text-translation\src\main\resources\application-xxx.yml
- 链接: https://pan.baidu.com/s/1I_NN88bXR_ZwvirjZKePQw?pwd=kqva 
- 
```bash
# Model URI
model:
  # 模型路径,注意路径最后要有分隔符
  # /Users/calvin/products/4_apps/trans_nllb_sdk/text-translation/models/
  modelPath: D:\\ai_projects\\products\\2_nlp_sdks\\trans_nllb_sdk\\models\\
  # 模型名字
  modelName: traced_translation.pt
  # 设置为 CPU 核心数 (Core Number)
  poolSize: 4

```

### 202种语言互相翻译 Web 应用

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/AIAS/nlp_sdks/translation.jpeg)


#### 主要特性
- 支持202种语言互相翻译。
- 支持 CPU / GPU



### 1. 前端部署

#### 1.1 安装运行：
```bash
# 安装依赖包
npm install
# 运行
npm run dev
```

#### 1.2 构建dist安装包：
```bash
npm run build:prod
```

#### 1.3 nginx部署运行(mac环境为例)：
```bash
cd /usr/local/etc/nginx/
vi /usr/local/etc/nginx/nginx.conf
# 编辑nginx.conf

    server {
        listen       8080;
        server_name  localhost;

        location / {
            root   /Users/calvin/Documents/text_translation/dist/;
            index  index.html index.htm;
        }
     ......
     
# 重新加载配置：
sudo nginx -s reload 

# 部署应用后，重启：
cd /usr/local/Cellar/nginx/1.19.6/bin

# 快速停止
sudo nginx -s stop

# 启动
sudo nginx     
```

## 2. 后端jar部署
#### 2.1 环境要求：
- 系统JDK 1.8+
- 根据实际路径更新配置文件 application.yml       

```bash
# Model URI
model:
  # 模型路径,注意路径最后要有分隔符
  # /Users/calvin/products/4_apps/trans_nllb_sdk/text-translation/models/
  modelPath: D:\\ai_projects\\products\\2_nlp_sdks\\trans_nllb_sdk\\models\\
  # 模型名字
  modelName: traced_translation.pt
  # 设置为 CPU 核心数 (Core Number)
  poolSize: 4

# 参数配置
config:
  # 输出文字最大长度
  maxLength: 128
  # 是否启用显卡 GPU, true, false
  gpu: false
    ...
```

#### 2.2 运行程序：
```bash
# 运行程序

java -jar text-translation-0.1.0.jar

```


## 4. 打开浏览器
- 输入地址： http://localhost:8090

### 文本翻译
- 选择源语言
- 目标语言
- 输入文字
- 点击查询

然后可以看到返回的翻译结果。

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/products/assets/nllb.png)

## 5. 帮助信息
- swagger接口文档:  
- http://localhost:8089/swagger-ui.html

![Screenshot](https://aias-home.oss-cn-beijing.aliyuncs.com/products/assets/nllb_swagger.png)



### 官网：
[官网链接](http://www.aias.top/)



#### 帮助文档：
- http://aias.top/guides.html
- 1.性能优化常见问题:
- http://aias.top/AIAS/guides/performance.html
- 2.引擎配置（包括CPU，GPU在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/engine_config.html
- 3.模型加载方式（在线自动加载，及本地配置）:
- http://aias.top/AIAS/guides/load_model.html
- 4.Windows环境常见问题:
- http://aias.top/AIAS/guides/windows.html


## 语言编码
### 详情请参考excel文档： 语言编码.xlsx

Language | Code
---|---
Acehnese (Arabic script) | ace_Arab
Acehnese (Latin script) | ace_Latn
Mesopotamian Arabic | acm_Arab
Ta’izzi-Adeni Arabic | acq_Arab
Tunisian Arabic | aeb_Arab
Afrikaans | afr_Latn
South Levantine Arabic | ajp_Arab
Akan | aka_Latn
Amharic | amh_Ethi
North Levantine Arabic | apc_Arab
Modern Standard Arabic | arb_Arab
Modern Standard Arabic (Romanized) | arb_Latn
Najdi Arabic | ars_Arab
Moroccan Arabic | ary_Arab
Egyptian Arabic | arz_Arab
Assamese | asm_Beng
Asturian | ast_Latn
Awadhi | awa_Deva
Central Aymara | ayr_Latn
South Azerbaijani | azb_Arab
North Azerbaijani | azj_Latn
Bashkir | bak_Cyrl
Bambara | bam_Latn
Balinese | ban_Latn
Belarusian | bel_Cyrl
Bemba | bem_Latn
Bengali | ben_Beng
Bhojpuri | bho_Deva
Banjar (Arabic script) | bjn_Arab
Banjar (Latin script) | bjn_Latn
Standard Tibetan | bod_Tibt
Bosnian | bos_Latn
Buginese | bug_Latn
Bulgarian | bul_Cyrl
Catalan | cat_Latn
Cebuano | ceb_Latn
Czech | ces_Latn
Chokwe | cjk_Latn
Central Kurdish | ckb_Arab
Crimean Tatar | crh_Latn
Welsh | cym_Latn
Danish | dan_Latn
German | deu_Latn
Southwestern Dinka | dik_Latn
Dyula | dyu_Latn
Dzongkha | dzo_Tibt
Greek | ell_Grek
English | eng_Latn
Esperanto | epo_Latn
Estonian | est_Latn
Basque | eus_Latn
Ewe | ewe_Latn
Faroese | fao_Latn
Fijian | fij_Latn
Finnish | fin_Latn
Fon | fon_Latn
French | fra_Latn
Friulian | fur_Latn
Nigerian Fulfulde | fuv_Latn
Scottish Gaelic | gla_Latn
Irish | gle_Latn
Galician | glg_Latn
Guarani | grn_Latn
Gujarati | guj_Gujr
Haitian Creole | hat_Latn
Hausa | hau_Latn
Hebrew | heb_Hebr
Hindi | hin_Deva
Chhattisgarhi | hne_Deva
Croatian | hrv_Latn
Hungarian | hun_Latn
Armenian | hye_Armn
Igbo | ibo_Latn
Ilocano | ilo_Latn
Indonesian | ind_Latn
Icelandic | isl_Latn
Italian | ita_Latn
Javanese | jav_Latn
Japanese | jpn_Jpan
Kabyle | kab_Latn
Jingpho | kac_Latn
Kamba | kam_Latn
Kannada | kan_Knda
Kashmiri (Arabic script) | kas_Arab
Kashmiri (Devanagari script) | kas_Deva
Georgian | kat_Geor
Central Kanuri (Arabic script) | knc_Arab
Central Kanuri (Latin script) | knc_Latn
Kazakh | kaz_Cyrl
Kabiyè | kbp_Latn
Kabuverdianu | kea_Latn
Khmer | khm_Khmr
Kikuyu | kik_Latn
Kinyarwanda | kin_Latn
Kyrgyz | kir_Cyrl
Kimbundu | kmb_Latn
Northern Kurdish | kmr_Latn
Kikongo | kon_Latn
Korean | kor_Hang
Lao | lao_Laoo
Ligurian | lij_Latn
Limburgish | lim_Latn
Lingala | lin_Latn
Lithuanian | lit_Latn
Lombard | lmo_Latn
Latgalian | ltg_Latn
Luxembourgish | ltz_Latn
Luba-Kasai | lua_Latn
Ganda | lug_Latn
Luo | luo_Latn
Mizo | lus_Latn
Standard Latvian | lvs_Latn
Magahi | mag_Deva
Maithili | mai_Deva
Malayalam | mal_Mlym
Marathi | mar_Deva
Minangkabau (Arabic script) | min_Arab
Minangkabau (Latin script) | min_Latn
Macedonian | mkd_Cyrl
Plateau Malagasy | plt_Latn
Maltese | mlt_Latn
Meitei (Bengali script) | mni_Beng
Halh Mongolian | khk_Cyrl
Mossi | mos_Latn
Maori | mri_Latn
Burmese | mya_Mymr
Dutch | nld_Latn
Norwegian Nynorsk | nno_Latn
Norwegian Bokmål | nob_Latn
Nepali | npi_Deva
Northern Sotho | nso_Latn
Nuer | nus_Latn
Nyanja | nya_Latn
Occitan | oci_Latn
West Central Oromo | gaz_Latn
Odia | ory_Orya
Pangasinan | pag_Latn
Eastern Panjabi | pan_Guru
Papiamento | pap_Latn
Western Persian | pes_Arab
Polish | pol_Latn
Portuguese | por_Latn
Dari | prs_Arab
Southern Pashto | pbt_Arab
Ayacucho Quechua | quy_Latn
Romanian | ron_Latn
Rundi | run_Latn
Russian | rus_Cyrl
Sango | sag_Latn
Sanskrit | san_Deva
Santali | sat_Olck
Sicilian | scn_Latn
Shan | shn_Mymr
Sinhala | sin_Sinh
Slovak | slk_Latn
Slovenian | slv_Latn
Samoan | smo_Latn
Shona | sna_Latn
Sindhi | snd_Arab
Somali | som_Latn
Southern Sotho | sot_Latn
Spanish | spa_Latn
Tosk Albanian | als_Latn
Sardinian | srd_Latn
Serbian | srp_Cyrl
Swati | ssw_Latn
Sundanese | sun_Latn
Swedish | swe_Latn
Swahili | swh_Latn
Silesian | szl_Latn
Tamil | tam_Taml
Tatar | tat_Cyrl
Telugu | tel_Telu
Tajik | tgk_Cyrl
Tagalog | tgl_Latn
Thai | tha_Thai
Tigrinya | tir_Ethi
Tamasheq (Latin script) | taq_Latn
Tamasheq (Tifinagh script) | taq_Tfng
Tok Pisin | tpi_Latn
Tswana | tsn_Latn
Tsonga | tso_Latn
Turkmen | tuk_Latn
Tumbuka | tum_Latn
Turkish | tur_Latn
Twi | twi_Latn
Central Atlas Tamazight | tzm_Tfng
Uyghur | uig_Arab
Ukrainian | ukr_Cyrl
Umbundu | umb_Latn
Urdu | urd_Arab
Northern Uzbek | uzn_Latn
Venetian | vec_Latn
Vietnamese | vie_Latn
Waray | war_Latn
Wolof | wol_Latn
Xhosa | xho_Latn
Eastern Yiddish | ydd_Hebr
Yoruba | yor_Latn
Yue Chinese | yue_Hant
Chinese (Simplified) | zho_Hans
Chinese (Traditional) | zho_Hant
Standard Malay | zsm_Latn
Zulu | zul_Latn