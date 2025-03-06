# Spring Boot 文件下载演示项目

这是一个基于Spring Boot的文件下载演示项目，展示了如何使用前端的XMLHttpRequest（XHR）或Fetch API实现文件下载功能，并结合后端的大数据量Excel文件生成和下载处理。

## 主要功能

1. 前端文件下载实现
   - 支持XMLHttpRequest（XHR）方式下载
   - 支持Fetch API方式下载
   - 支持GET和POST两种请求方式
   - 完善的错误处理机制

2. 后端Excel文件处理
   - 使用Apache POI的SXSSF模式处理大数据量
   - 支持生成不同数据量的Excel文件
   - 内存优化的数据处理方式
   - 异常处理和错误响应

## 技术栈

- 后端
  - Spring Boot 2.7.18
  - Apache POI 5.2.3（Excel处理）
  - Lombok（代码简化）
  - Spring Boot DevTools（开发热重载）

- 前端
  - 原生JavaScript
  - XMLHttpRequest/Fetch API
  - jQuery 3.7.1

## 使用方法

### 1. 下载方式

项目提供了多种下载实现方式：

```javascript
// 使用XHR下载（GET方式）
downloadWithXHR('/api/files/download', { action: 'simple' })
  .then(blob => triggerDownload(blob, 'example.xlsx'))
  .catch(error => console.error('下载失败:', error));

// 使用XHR下载（POST方式）
downloadWithXHRPost('/api/files/download', { action: 'simple' })
  .then(blob => triggerDownload(blob, 'example.xlsx'))
  .catch(error => console.error('下载失败:', error));

// 使用Fetch下载（GET方式）
downloadWithFetch('/api/files/download', { action: 'simple' })
  .then(blob => triggerDownload(blob, 'example.xlsx'))
  .catch(error => console.error('下载失败:', error));

// 使用Fetch下载（POST方式）
downloadWithFetchPost('/api/files/download', { action: 'simple' })
  .then(blob => triggerDownload(blob, 'example.xlsx'))
  .catch(error => console.error('下载失败:', error));
```

### 2. 参数说明

下载接口支持以下参数：

- `action`: 控制下载行为
  - `simple`: 生成100行数据的Excel文件
  - `large`: 生成100000行数据的Excel文件
  - `error`: 触发错误处理流程

### 3. 错误处理

项目实现了完善的错误处理机制：

- 前端统一的错误处理
- 支持服务器端错误信息的解析和展示
- 网络错误的处理
- 文件处理异常的处理

## 项目特点

1. 内存优化
   - 使用SXSSF模式处理大数据量Excel
   - 控制内存中的数据行数
   - 自动清理临时文件

2. 错误处理
   - 统一的错误响应格式
   - 详细的错误信息
   - 支持中文错误消息

3. 代码质量
   - 规范的代码组织
   - 完善的注释
   - 统一的编码风格

## 开发环境

- JDK 1.8
- Maven 3.x
- Spring Boot 2.7.18

## 运行项目

1. 克隆项目到本地
2. 使用Maven安装依赖：`mvn install`
3. 运行Spring Boot应用：`mvn spring-boot:run`
4. 访问：`http://localhost:8080`

## 注意事项

1. 大数据量下载时注意服务器内存配置
2. 文件下载大小限制为10MB（可在配置文件中调整）
3. 开发模式下支持热重载
4. 建议在下载大文件时使用POST方式