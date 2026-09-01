
from typing import List
# if __name__ == '__main__':
    # print(twoNumSum([1,2,3,4,5,6,7,8,9], 9))

class Solution:
    @classmethod
    def twoNumSum(self, nums: List[int], target: int) -> List[int]:
        '''

        :param self:
        :param nums:
        :param target:
        :return:
        '''
        hashmap = {}
        for i, num in enumerate(nums):
            complement = target - num
            if complement in hashmap:
                return [hashmap[complement], i]
            hashmap[num] = i
        raise ValueError("Not found")

# print(twoNumSum([1,2,3,4,5,6,7,8,9], 9))