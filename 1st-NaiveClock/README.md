# Naive Clock

按照作业要求，使用 Java 和 Kotlin 混编（除了 MainActivity 是 Java 外其他都是 Kotlin）实现了一个简单的 Android 原生的时钟 App，包含

1. 表盘时钟
2. 数字时钟
3. 世界时间

三个页面，并用 ViewPager 组织在一起。使用的组件全部为课程内容涉及的 Android 原生组件和接口（得益于 Kotlin 可以无缝调用任何 Java API），完全符合作业要求。况且段老师的课件上也提到了 Kotlin，充分说明可以用（

代码实际上从提供的样板修改得来，但是除了 `res` 下的图标和部分样式其他全都被我重写或者改得和原来完全看不出关系。

该项目直接在我本机的 Android Studio Iguana 2023.2.1 Patch 2 中打开可以编译，并在 Google Pixel 3a (Android 14.0) 模拟器和华为 Mate 30 (Android 12.0) 上正常运行，运行结果符合预期。如果出现任何问题烦请直接联系我。
