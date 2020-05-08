# TextViewForHTml
Android的自带TextView对于Html的标签解析是有限的，所以就扩展来处理自定义标签
# 兼容标签
## 除了本身自带解析的标签外，添加了对列表的支持
```bash
1.<ol>
2.<dd>
3.<ul>
4.<img src/>
```
## 添加对<style>支持，其中包括：<span style="color:#000000">和<p style="text-align:center">等支持
# 不足
## 后续将添加对GIF的显示
