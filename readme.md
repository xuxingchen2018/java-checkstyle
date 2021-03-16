### 自定义Android，Java代码规范检测    
主要检测RecyclerView的ViewHolder类注释中是否关联了布局文件，如果没有关联，那么代码规范不通过，
注意，只有继承自RecyclerViewHolder的类才会进行检测，在多层继承时，只能找到父类查看布局文件