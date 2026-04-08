# 前端设计文档

## 1. 技术栈

- **框架**: Vue 3 (Composition API)
- **UI组件库**: Element Plus
- **状态管理**: Pinia
- **路由**: Vue Router 4
- **HTTP客户端**: Axios
- **图片上传**: vue-upload-component
- **拖拽排序**: vuedraggable
- **图片裁剪**: vue-advanced-cropper
- **打包工具**: Vite

## 2. 目录结构

```
frontend/
├── public/
│   ├── index.html
│   └── favicon.ico
├── src/
│   ├── assets/              # 静态资源
│   │   ├── images/
│   │   ├── styles/
│   │   │   ├── common.scss
│   │   │   └── variables.scss
│   │   └── icons/
│   ├── components/          # 通用组件
│   │   ├── ImageUpload.vue
│   │   ├── ImagePreview.vue
│   │   ├── LoadingOverlay.vue
│   │   └── ProgressBar.vue
│   ├── views/               # 页面组件
│   │   ├── ProductCreate/
│   │   │   ├── index.vue
│   │   │   ├── StepBasicInfo.vue
│   │   │   ├── StepPainPoints.vue
│   │   │   ├── StepRequirements.vue
│   │   │   ├── StepComponentImages.vue
│   │   │   ├── StepGeneration.vue
│   │   │   └── StepPreview.vue
│   │   ├── ProductList.vue
│   │   ├── ProductDetail.vue
│   │   └── Dashboard.vue
│   ├── api/                 # API接口
│   │   ├── product.js
│   │   ├── image.js
│   │   ├── ai.js
│   │   └── user.js
│   ├── store/               # 状态管理
│   │   ├── index.js
│   │   ├── modules/
│   │   │   ├── user.js
│   │   │   ├── product.js
│   │   │   └── generation.js
│   ├── router/              # 路由配置
│   │   └── index.js
│   ├── utils/               # 工具函数
│   │   ├── request.js
│   │   ├── auth.js
│   │   ├── imageUtils.js
│   │   └── validation.js
│   ├── App.vue
│   └── main.js
├── package.json
└── vite.config.js
```

## 3. 核心页面设计

### 3.1 产品创建页面（主工作流）

**路由**: `/product/create`

**组件**: `views/ProductCreate/index.vue`

#### 步骤式布局（Steps）

```vue
<template>
  <div class="product-create-container">
    <el-card>
      <el-steps :active="currentStep" finish-status="success" align-center>
        <el-step title="基础信息" />
        <el-step title="用户痛点" />
        <el-step title="用户需求" />
        <el-step title="部件图片" />
        <el-step title="AI生成" />
        <el-step title="预览调整" />
      </el-steps>

      <div class="step-content">
        <component 
          :is="currentStepComponent" 
          v-model="formData"
          @next="handleNext"
          @prev="handlePrev"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import StepBasicInfo from './StepBasicInfo.vue'
import StepPainPoints from './StepPainPoints.vue'
import StepRequirements from './StepRequirements.vue'
import StepComponentImages from './StepComponentImages.vue'
import StepGeneration from './StepGeneration.vue'
import StepPreview from './StepPreview.vue'

const currentStep = ref(0)
const formData = ref({
  basicInfo: {},
  painPoints: [],
  requirements: [],
  components: [],
  generatedImages: []
})

const stepComponents = [
  StepBasicInfo,
  StepPainPoints,
  StepRequirements,
  StepComponentImages,
  StepGeneration,
  StepPreview
]

const currentStepComponent = computed(() => stepComponents[currentStep.value])

const handleNext = () => {
  if (currentStep.value < 5) {
    currentStep.value++
  }
}

const handlePrev = () => {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}
</script>
```

### 3.2 Step 1: 基础信息表单

**组件**: `StepBasicInfo.vue`

```vue
<template>
  <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
    <el-form-item label="产品名称" prop="name" required>
      <el-input 
        v-model="form.name" 
        placeholder="例如：俄罗斯风格发饰套装"
        maxlength="100"
        show-word-limit
      />
    </el-form-item>

    <el-form-item label="产品类目" prop="category" required>
      <el-select v-model="form.category" placeholder="请选择">
        <el-option label="发饰" value="发饰" />
        <el-option label="项链" value="项链" />
        <el-option label="手链" value="手链" />
        <el-option label="耳环" value="耳环" />
        <el-option label="其他配饰" value="其他" />
      </el-select>
    </el-form-item>

    <el-form-item label="目标市场" prop="targetMarket" required>
      <el-select v-model="form.targetMarket" placeholder="请选择">
        <el-option label="俄罗斯" value="俄罗斯" />
        <el-option label="欧洲" value="欧洲" />
        <el-option label="北美" value="北美" />
        <el-option label="其他" value="其他" />
      </el-select>
    </el-form-item>

    <el-form-item label="产品描述" prop="description">
      <el-input 
        v-model="form.description" 
        type="textarea" 
        :rows="5"
        placeholder="详细描述产品特点、材质、风格等"
        maxlength="1000"
        show-word-limit
      />
    </el-form-item>

    <el-form-item label="价格" prop="price">
      <el-input-number 
        v-model="form.price" 
        :min="0" 
        :step="0.01"
        :precision="2"
      />
      <span style="margin-left: 10px;">卢布</span>
    </el-form-item>

    <el-form-item label="关键词标签">
      <el-tag
        v-for="tag in form.keywords"
        :key="tag"
        closable
        @close="handleRemoveTag(tag)"
        style="margin-right: 10px;"
      >
        {{ tag }}
      </el-tag>
      <el-input
        v-if="inputVisible"
        ref="inputRef"
        v-model="inputValue"
        size="small"
        style="width: 100px;"
        @keyup.enter="handleInputConfirm"
        @blur="handleInputConfirm"
      />
      <el-button v-else size="small" @click="showInput">
        + 添加标签
      </el-button>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="handleNext">
        下一步：用户痛点
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, reactive } from 'vue'

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue', 'next'])

const form = reactive(props.modelValue.basicInfo || {
  name: '',
  category: '发饰',
  targetMarket: '俄罗斯',
  description: '',
  price: 0,
  keywords: []
})

const formRef = ref(null)
const inputVisible = ref(false)
const inputValue = ref('')
const inputRef = ref(null)

const rules = {
  name: [
    { required: true, message: '请输入产品名称', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '请选择产品类目', trigger: 'change' }
  ],
  targetMarket: [
    { required: true, message: '请选择目标市场', trigger: 'change' }
  ]
}

const handleNext = async () => {
  await formRef.value.validate()
  emit('update:modelValue', { ...props.modelValue, basicInfo: form })
  emit('next')
}

const handleRemoveTag = (tag) => {
  form.keywords.splice(form.keywords.indexOf(tag), 1)
}

const showInput = () => {
  inputVisible.value = true
  nextTick(() => {
    inputRef.value.focus()
  })
}

const handleInputConfirm = () => {
  if (inputValue.value && !form.keywords.includes(inputValue.value)) {
    form.keywords.push(inputValue.value)
  }
  inputVisible.value = false
  inputValue.value = ''
}
</script>
```

### 3.3 Step 2: 用户痛点表单

**组件**: `StepPainPoints.vue`

```vue
<template>
  <div class="pain-points-container">
    <el-alert 
      title="提示" 
      type="info" 
      :closable="false"
      style="margin-bottom: 20px;"
    >
      请添加通过竞品分析、用户评论等渠道收集到的用户痛点，这将帮助AI生成更具针对性的产品图片
    </el-alert>

    <el-table :data="painPoints" style="width: 100%">
      <el-table-column type="index" label="#" width="50" />
      <el-table-column label="痛点内容" min-width="300">
        <template #default="{ row }">
          <el-input 
            v-model="row.content" 
            type="textarea"
            :rows="2"
            placeholder="例如：现有发饰容易滑落，固定不牢"
          />
        </template>
      </el-table-column>
      <el-table-column label="来源" width="150">
        <template #default="{ row }">
          <el-select v-model="row.source" placeholder="选择来源">
            <el-option label="竞品分析" value="竞品分析" />
            <el-option label="用户评论" value="用户评论" />
            <el-option label="市场调研" value="市场调研" />
            <el-option label="售后反馈" value="售后反馈" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="优先级" width="120">
        <template #default="{ row }">
          <el-select v-model="row.priority" placeholder="优先级">
            <el-option label="高" value="high">
              <el-tag type="danger">高</el-tag>
            </el-option>
            <el-option label="中" value="medium">
              <el-tag type="warning">中</el-tag>
            </el-option>
            <el-option label="低" value="low">
              <el-tag type="info">低</el-tag>
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ $index }">
          <el-button 
            type="danger" 
            size="small" 
            @click="removePainPoint($index)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-button 
      type="primary" 
      style="margin-top: 20px;" 
      @click="addPainPoint"
    >
      + 添加痛点
    </el-button>

    <el-divider />

    <el-button @click="$emit('prev')">上一步</el-button>
    <el-button type="primary" @click="handleNext">
      下一步：用户需求
    </el-button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue', 'next', 'prev'])

const painPoints = ref(props.modelValue.painPoints || [])

const addPainPoint = () => {
  painPoints.value.push({
    content: '',
    source: '竞品分析',
    priority: 'medium'
  })
}

const removePainPoint = (index) => {
  painPoints.value.splice(index, 1)
}

const handleNext = () => {
  if (painPoints.value.length === 0) {
    ElMessage.warning('请至少添加一个用户痛点')
    return
  }
  
  const hasEmptyContent = painPoints.value.some(p => !p.content.trim())
  if (hasEmptyContent) {
    ElMessage.warning('请填写所有痛点内容')
    return
  }

  emit('update:modelValue', { ...props.modelValue, painPoints: painPoints.value })
  emit('next')
}
</script>
```

### 3.4 Step 4: 部件图片上传

**组件**: `StepComponentImages.vue`

```vue
<template>
  <div class="component-images-container">
    <el-alert 
      title="提示" 
      type="info" 
      :closable="false"
      style="margin-bottom: 20px;"
    >
      请上传产品各个部件的图片，每个部件可上传多张不同角度的照片。建议图片清晰、背景干净。
    </el-alert>

    <div 
      v-for="(component, index) in components" 
      :key="index"
      class="component-item"
    >
      <el-card>
        <template #header>
          <div class="component-header">
            <span>部件 {{ index + 1 }}</span>
            <el-button 
              type="danger" 
              size="small" 
              @click="removeComponent(index)"
            >
              删除部件
            </el-button>
          </div>
        </template>

        <el-form :model="component" label-width="100px">
          <el-form-item label="部件名称">
            <el-input 
              v-model="component.name" 
              placeholder="例如：主发夹"
            />
          </el-form-item>

          <el-form-item label="部件描述">
            <el-input 
              v-model="component.description" 
              type="textarea"
              :rows="2"
              placeholder="例如：大号蝴蝶结装饰发夹，带珍珠点缀"
            />
          </el-form-item>

          <el-form-item label="部件图片">
            <el-upload
              :action="uploadAction"
              :headers="uploadHeaders"
              list-type="picture-card"
              :on-success="(response) => handleUploadSuccess(response, index)"
              :on-remove="(file) => handleRemove(file, index)"
              :before-upload="beforeUpload"
              multiple
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
            <div class="upload-tip">
              建议上传多张不同角度的图片，支持JPG/PNG格式，单张不超过5MB
            </div>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <el-button 
      type="primary" 
      style="margin-top: 20px;" 
      @click="addComponent"
    >
      + 添加部件
    </el-button>

    <el-divider />

    <el-button @click="$emit('prev')">上一步</el-button>
    <el-button type="primary" @click="handleNext">
      下一步：开始生成
    </el-button>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue', 'next', 'prev'])

const components = ref(props.modelValue.components || [])

const uploadAction = computed(() => {
  return `${import.meta.env.VITE_API_BASE_URL}/api/image/upload`
})

const uploadHeaders = computed(() => {
  return {
    Authorization: `Bearer ${getToken()}`
  }
})

const addComponent = () => {
  components.value.push({
    name: '',
    description: '',
    images: []
  })
}

const removeComponent = (index) => {
  components.value.splice(index, 1)
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

const handleUploadSuccess = (response, componentIndex) => {
  if (response.code === 200) {
    components.value[componentIndex].images.push({
      url: response.data.url,
      thumbnail: response.data.thumbnail
    })
    ElMessage.success('上传成功')
  } else {
    ElMessage.error('上传失败')
  }
}

const handleRemove = (file, componentIndex) => {
  const images = components.value[componentIndex].images
  const index = images.findIndex(img => img.url === file.url)
  if (index > -1) {
    images.splice(index, 1)
  }
}

const handleNext = () => {
  if (components.value.length === 0) {
    ElMessage.warning('请至少添加一个产品部件')
    return
  }

  const hasEmptyName = components.value.some(c => !c.name.trim())
  if (hasEmptyName) {
    ElMessage.warning('请填写所有部件名称')
    return
  }

  const hasNoImages = components.value.some(c => c.images.length === 0)
  if (hasNoImages) {
    ElMessage.warning('请为每个部件上传至少一张图片')
    return
  }

  emit('update:modelValue', { ...props.modelValue, components: components.value })
  emit('next')
}
</script>

<style scoped>
.component-images-container {
  max-width: 1000px;
  margin: 0 auto;
}

.component-item {
  margin-bottom: 20px;
}

.component-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upload-tip {
  color: #999;
  font-size: 12px;
  margin-top: 10px;
}
</style>
```

### 3.5 Step 5: AI生成进度

**组件**: `StepGeneration.vue`

```vue
<template>
  <div class="generation-container">
    <el-card v-if="!isGenerating && !taskId">
      <div class="generation-summary">
        <h3>准备生成产品图片</h3>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="产品名称">
            {{ formData.basicInfo.name }}
          </el-descriptions-item>
          <el-descriptions-item label="产品类目">
            {{ formData.basicInfo.category }}
          </el-descriptions-item>
          <el-descriptions-item label="目标市场">
            {{ formData.basicInfo.targetMarket }}
          </el-descriptions-item>
          <el-descriptions-item label="用户痛点数">
            {{ formData.painPoints.length }} 个
          </el-descriptions-item>
          <el-descriptions-item label="用户需求数">
            {{ formData.requirements.length }} 个
          </el-descriptions-item>
          <el-descriptions-item label="产品部件数">
            {{ formData.components.length }} 个
          </el-descriptions-item>
          <el-descriptions-item label="总图片数">
            {{ totalImages }} 张
          </el-descriptions-item>
        </el-descriptions>

        <el-alert 
          title="生成说明" 
          type="info" 
          :closable="false"
          style="margin-top: 20px;"
        >
          <ul>
            <li>将生成10张电商主图，包括：</li>
            <li>• 前3张：俄罗斯风格美女佩戴效果图</li>
            <li>• 中3张：产品细节与差异化展示</li>
            <li>• 后4张：全配件图、使用场景、规格参数、品质保证</li>
            <li>• 预计耗时：3-5分钟</li>
            <li>• 所有图片将自动压缩至1MB以内（OZON平台要求）</li>
          </ul>
        </el-alert>

        <el-button 
          type="primary" 
          size="large"
          style="margin-top: 30px;"
          @click="startGeneration"
        >
          开始生成
        </el-button>
      </div>
    </el-card>

    <el-card v-else-if="isGenerating">
      <div class="generation-progress">
        <h3>正在生成中...</h3>
        
        <el-progress 
          :percentage="progress" 
          :status="progressStatus"
          :stroke-width="20"
        />

        <div class="progress-text">
          {{ progressText }}
        </div>

        <el-timeline style="margin-top: 30px;">
          <el-timeline-item 
            v-for="log in generationLogs" 
            :key="log.time"
            :timestamp="log.time"
            :type="log.type"
          >
            {{ log.message }}
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-card>

    <el-card v-else>
      <el-result
        icon="success"
        title="生成完成！"
        sub-title="所有图片已成功生成并压缩"
      >
        <template #extra>
          <el-button type="primary" @click="$emit('next')">
            前往预览
          </el-button>
        </template>
      </el-result>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { startAIGeneration, queryTaskStatus } from '@/api/ai'
import { ElMessage } from 'element-plus'

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue', 'next', 'prev'])

const formData = computed(() => props.modelValue)
const isGenerating = ref(false)
const taskId = ref(null)
const progress = ref(0)
const progressText = ref('')
const generationLogs = ref([])

const totalImages = computed(() => {
  return formData.value.components.reduce((sum, c) => sum + c.images.length, 0)
})

const progressStatus = computed(() => {
  if (progress.value === 100) return 'success'
  if (progress.value > 0) return 'warning'
  return ''
})

const startGeneration = async () => {
  try {
    isGenerating.value = true
    
    // 调用后端API开始生成
    const response = await startAIGeneration({
      productInfo: formData.value.basicInfo,
      painPoints: formData.value.painPoints,
      requirements: formData.value.requirements,
      components: formData.value.components
    })

    taskId.value = response.data.taskId
    
    addLog('任务已创建', 'primary')
    
    // 开始轮询任务状态
    pollTaskStatus()
    
  } catch (error) {
    ElMessage.error('启动生成失败: ' + error.message)
    isGenerating.value = false
  }
}

const pollTaskStatus = async () => {
  const timer = setInterval(async () => {
    try {
      const response = await queryTaskStatus(taskId.value)
      const task = response.data
      
      progress.value = task.progress
      progressText.value = task.statusMessage || ''
      
      if (task.logs && task.logs.length > 0) {
        task.logs.forEach(log => {
          if (!generationLogs.value.find(l => l.time === log.time)) {
            addLog(log.message, log.type)
          }
        })
      }
      
      if (task.status === 'COMPLETED') {
        clearInterval(timer)
        addLog('所有图片生成完成!', 'success')
        
        // 保存生成结果
        emit('update:modelValue', {
          ...formData.value,
          generatedImages: task.images,
          taskId: taskId.value
        })
        
        setTimeout(() => {
          emit('next')
        }, 2000)
      } else if (task.status === 'FAILED') {
        clearInterval(timer)
        addLog('生成失败: ' + task.errorMessage, 'danger')
        ElMessage.error('生成失败，请重试')
        isGenerating.value = false
      }
      
    } catch (error) {
      clearInterval(timer)
      ElMessage.error('查询任务状态失败')
      isGenerating.value = false
    }
  }, 2000) // 每2秒查询一次
}

const addLog = (message, type = 'info') => {
  generationLogs.value.push({
    message,
    type,
    time: new Date().toLocaleTimeString()
  })
}
</script>

<style scoped>
.generation-container {
  max-width: 800px;
  margin: 0 auto;
}

.generation-summary ul {
  padding-left: 20px;
  line-height: 2;
}

.generation-progress {
  text-align: center;
}

.progress-text {
  margin-top: 20px;
  font-size: 16px;
  color: #409EFF;
  font-weight: 500;
}
</style>
```

### 3.6 Step 6: 预览与调整

**组件**: `StepPreview.vue`

```vue
<template>
  <div class="preview-container">
    <el-alert 
      title="预览说明" 
      type="success" 
      :closable="false"
      style="margin-bottom: 20px;"
    >
      生成完成！您可以预览、调整顺序、重新生成单张或下载所有图片。所有图片已压缩至1MB以内。
    </el-alert>

    <div class="preview-grid">
      <draggable 
        v-model="images" 
        class="draggable-grid"
        item-key="id"
      >
        <template #item="{ element, index }">
          <el-card 
            class="image-card" 
            :body-style="{ padding: '0px' }"
            shadow="hover"
          >
            <div class="image-wrapper">
              <el-image 
                :src="element.url" 
                fit="cover"
                :preview-src-list="allImageUrls"
                :initial-index="index"
              >
                <template #error>
                  <div class="image-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              
              <div class="image-overlay">
                <el-tag size="small">图片 {{ index + 1 }}</el-tag>
              </div>
            </div>

            <div class="image-info">
              <div class="image-meta">
                <span>{{ element.type }}</span>
                <span>{{ formatFileSize(element.fileSize) }}</span>
              </div>
              
              <div class="image-actions">
                <el-button 
                  size="small" 
                  type="primary" 
                  @click="previewImage(index)"
                >
                  <el-icon><ZoomIn /></el-icon>
                </el-button>
                <el-button 
                  size="small" 
                  type="warning" 
                  @click="regenerateImage(index)"
                >
                  <el-icon><Refresh /></el-icon>
                </el-button>
                <el-button 
                  size="small" 
                  type="success" 
                  @click="downloadImage(element)"
                >
                  <el-icon><Download /></el-icon>
                </el-button>
              </div>
            </div>
          </el-card>
        </template>
      </draggable>
    </div>

    <el-divider />

    <div class="batch-actions">
      <el-button type="primary" size="large" @click="downloadAll">
        <el-icon><Download /></el-icon>
        下载全部图片
      </el-button>
      <el-button type="warning" size="large" @click="regenerateAll">
        <el-icon><Refresh /></el-icon>
        重新生成全部
      </el-button>
      <el-button type="success" size="large" @click="saveProduct">
        <el-icon><Check /></el-icon>
        保存产品
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import draggable from 'vuedraggable'
import { Picture, ZoomIn, Refresh, Download, Check } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { downloadImages, regenerateImage as regenerateImageApi } from '@/api/ai'
import { saveProduct as saveProductApi } from '@/api/product'

const props = defineProps(['modelValue'])
const emit = defineEmits(['update:modelValue', 'prev'])

const images = ref(props.modelValue.generatedImages || [])

const allImageUrls = computed(() => images.value.map(img => img.url))

const formatFileSize = (bytes) => {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB'
  return (bytes / 1024 / 1024).toFixed(2) + ' MB'
}

const previewImage = (index) => {
  // Element Plus 的 el-image 已经自带预览功能
}

const regenerateImage = async (index) => {
  try {
    await ElMessageBox.confirm(
      '确定要重新生成这张图片吗？',
      '确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const image = images.value[index]
    ElMessage.info('正在重新生成...')
    
    const response = await regenerateImageApi({
      productId: props.modelValue.productId,
      imageIndex: index + 1,
      imageType: image.type
    })

    images.value[index] = response.data
    ElMessage.success('重新生成成功')
    
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重新生成失败')
    }
  }
}

const downloadImage = (image) => {
  const link = document.createElement('a')
  link.href = image.url
  link.download = `image_${image.index}.jpg`
  link.click()
}

const downloadAll = async () => {
  try {
    ElMessage.info('正在打包下载...')
    
    const response = await downloadImages({
      taskId: props.modelValue.taskId,
      imageUrls: images.value.map(img => img.url)
    })

    // 下载ZIP文件
    const link = document.createElement('a')
    link.href = response.data.zipUrl
    link.download = `product_images_${Date.now()}.zip`
    link.click()

    ElMessage.success('下载成功')
  } catch (error) {
    ElMessage.error('下载失败')
  }
}

const regenerateAll = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要重新生成所有图片吗？这将需要几分钟时间。',
      '确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    ElMessage.info('正在重新生成全部图片...')
    // 跳转回生成步骤
    emit('update:modelValue', {
      ...props.modelValue,
      generatedImages: [],
      taskId: null
    })
    // 返回到生成步骤
    // 需要在父组件中处理
    
  } catch (error) {
    // 用户取消
  }
}

const saveProduct = async () => {
  try {
    ElMessage.info('正在保存产品...')
    
    const response = await saveProductApi({
      ...props.modelValue,
      images: images.value
    })

    ElMessage.success('保存成功！')
    
    // 跳转到产品详情页
    // router.push(`/product/${response.data.id}`)
    
  } catch (error) {
    ElMessage.error('保存失败')
  }
}
</script>

<style scoped>
.preview-container {
  max-width: 1400px;
  margin: 0 auto;
}

.preview-grid {
  margin: 20px 0;
}

.draggable-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.image-card {
  cursor: move;
  transition: all 0.3s;
}

.image-card:hover {
  transform: translateY(-5px);
}

.image-wrapper {
  position: relative;
  height: 300px;
  overflow: hidden;
}

.image-wrapper :deep(.el-image) {
  width: 100%;
  height: 100%;
}

.image-overlay {
  position: absolute;
  top: 10px;
  left: 10px;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 30px;
}

.image-info {
  padding: 15px;
}

.image-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 12px;
  color: #909399;
}

.image-actions {
  display: flex;
  gap: 5px;
}

.image-actions .el-button {
  flex: 1;
}

.batch-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin: 30px 0;
}
</style>
```

## 4. API接口封装

**文件**: `src/api/ai.js`

```javascript
import request from '@/utils/request'

// 开始AI生成任务
export function startAIGeneration(data) {
  return request({
    url: '/api/ai/generate',
    method: 'post',
    data
  })
}

// 查询任务状态
export function queryTaskStatus(taskId) {
  return request({
    url: `/api/ai/task/${taskId}`,
    method: 'get'
  })
}

// 重新生成单张图片
export function regenerateImage(data) {
  return request({
    url: '/api/ai/regenerate',
    method: 'post',
    data
  })
}

// 下载图片
export function downloadImages(data) {
  return request({
    url: '/api/ai/download',
    method: 'post',
    data,
    responseType: 'blob'
  })
}
```

## 5. 状态管理

**文件**: `src/store/modules/generation.js`

```javascript
import { defineStore } from 'pinia'

export const useGenerationStore = defineStore('generation', {
  state: () => ({
    currentProduct: null,
    generationHistory: []
  }),
  
  actions: {
    setCurrentProduct(product) {
      this.currentProduct = product
    },
    
    addToHistory(product) {
      this.generationHistory.unshift(product)
      if (this.generationHistory.length > 10) {
        this.generationHistory.pop()
      }
    }
  }
})
```

## 6. 响应式设计

- 支持桌面端（1920px, 1440px, 1280px）
- 支持平板（768px）
- 移动端适配（375px, 414px）

## 7. 性能优化

- 图片懒加载
- 虚拟滚动（大量图片时）
- 路由懒加载
- 组件按需加载
- Gzip压缩
- CDN加速

## 8. 用户体验优化

- Loading动画
- 骨架屏
- 上传进度条
- 操作确认提示
- 错误提示友好
- 快捷键支持
- 拖拽排序
- 批量操作
