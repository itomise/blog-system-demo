export const formatDate = (inputDate: string): string => {
  const date = new Date(inputDate)
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}
