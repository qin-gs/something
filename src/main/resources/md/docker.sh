#!/bin/zsh

set -e
names=("rs-ui" "rs-gateway" "rs-system" "rs-plat" "rs-fault")

# 复制 jar 包
function cp_jar() {

  # 先删除之前的             rm -f ./rs-gateway/rs-gateway.jar
  rm -f "./${1}/${1}.jar"
  # 复制 jar 到指定位置      cp ./jars/rs-gateway.jar ./rs-gateway/
  cp ".jars/${1}.jar" "./${1}/"
}

function cp_ui {
  # 删除之前的
  # 复制 zip
  # 解压 zip
  # 删除 zip
  #  return "${1}"
  return 0
}

# 构建并上传 jar
upload() {
  docker build -t "${1}:${2}" .
  docker push "${1}:${2}"
}

# 使用 @ 将数组转成列表，再次使用 # 获取数组长度
for num in $(seq 1 ${#names[*]}); do
  echo "${num}". "${names[${num}]}"
done

read -p "输入要构建并上传的项目(用空格分隔): " nums
read -p "输入标签(比如: 02.06): " tag

for num in $nums; do
  case "$num" in
  1)
    cp_ui "${names[${num}]}"
    ;;
  *)
    cp_jar "${names[${num}]}"
    ;;
  esac
  upload "${names[${num}]}" "${tag}"
done
