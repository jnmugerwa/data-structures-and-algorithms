def binarySearch(nums, target):
    '''
    Efficiently finds the index of a target in a list of numbers.
    If the target is not unique, expect any of the target indices to be returned.
    :param nums: A list of numbers
    :param target: A number
    :return: The index of the target, if present in nums, else -1
    '''
    low, high = 0, len(nums) - 1
    while low <= high:
        mid = low + (high - low) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            low = mid + 1
        else:
            high = mid - 1
    return -1

print(binarySearch([1,2,3,4,99,99,99], 99))