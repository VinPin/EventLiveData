# EventLiveData

一个可以防止数据倒灌的 `MutableLiveData` 子类，解决 Android 开发中常见的 LiveData 数据倒灌问题。

## 功能特性

- ✅ **防止数据倒灌**：普通观察者不会收到注册之前发送的事件
- ✅ **支持粘性事件**：粘性观察者可以收到之前发送的事件
- ✅ **生命周期感知**：自动感知 Activity/Fragment 的生命周期
- ✅ **简洁 API**：与原生 LiveData 使用方式一致，易于迁移

## 为什么需要 EventLiveData

在 Android 开发中，使用 `MutableLiveData` 时经常遇到以下问题：

1. **数据倒灌问题**：当 Activity 重新创建时（如屏幕旋转），观察者会收到之前已经处理过的数据
2. **事件消费问题**：某些事件只应该被消费一次（如导航事件、Toast 提示）

`EventLiveData` 通过版本号机制解决了这些问题。

## 使用方法

### 添加依赖

```groovy
dependencies {
    implementation project(':eventlivedata')
}
```

### 创建 EventLiveData

```kotlin
class MyViewModel : ViewModel() {
    val eventLiveData = EventLiveData<String>()
    
    fun sendEvent(message: String) {
        eventLiveData.setValue(message)
    }
}
```

### 普通观察（非粘性）

普通观察者**不会**收到注册之前发送的事件，适用于一次性事件：

```kotlin
viewModel.eventLiveData.observe(this) { message ->
    // 只处理注册之后发送的事件
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
```

### 粘性观察

粘性观察者**会**收到之前发送的最新事件：

```kotlin
viewModel.eventLiveData.observeSticky(this) { message ->
    // 会收到注册之前发送的最新事件
    textView.text = message
}
```

### 生命周期无关观察

```kotlin
// 非粘性
val observer = viewModel.eventLiveData.observeForeverEvent { message ->
    // 处理事件
}

// 粘性
val stickyObserver = viewModel.eventLiveData.observeStickyForeverEvent { message ->
    // 处理事件
}

// 取消观察
viewModel.eventLiveData.removeObserver(observer)
```

## API 文档

### EventLiveData

| 方法 | 描述 |
|------|------|
| `setValue(value: T)` | 设置值，版本号递增 |
| `postValue(value: T)` | 异步设置值，版本号递增 |
| `observe(owner, observer)` | 普通观察，非粘性 |
| `observeSticky(owner, observer)` | 粘性观察 |
| `observeForeverEvent(observer)` | 生命周期无关观察，非粘性 |
| `observeStickyForeverEvent(observer)` | 生命周期无关观察，粘性 |
| `getCurrentVersion()` | 获取当前版本号 |
