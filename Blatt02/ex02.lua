------!!!!!!!!!!!!!!!
----- Array indices start at 1 !!!!
----- some ppl just want to see the world burn ....
------!!!!!!!!!!!!!!!

---------------------------------- Utils ----------------------------------
---- start: Queue, inspiration: https://subscription.packtpub.com/book/game-development/9781849515504/1/ch01lvl1sec12/making-a-queue
local function Queue()
    local out = {}
    -- zero indexed
    local first, last = 0, -1
    local index = -1
    out.add = function(item)
      last = last + 1
      out[last] = item
    end
    out.del = function()
      if first <= last then
        local value = out[first]
        out[first] = nil
        first = first + 1
        return value
      end
    end
    out.first = function()
      return out[first] 
    end
    out.iterator = function()
      return function()
        index = index + 1
        index = math.max(first,index)
        if index <= #out and index < last then
          return tostring(out[index])
        else if index == last then
          return tostring(out[last])
        end
      end
        --reset after iterating
        index = -1
      end
    end
    setmetatable(out, {
      __len = function()
        return (last-first+1)
      end,
    })
    return out
end
--#####################################

--start: Stack, inspiration: https://subscription.packtpub.com/book/game-development/9781849515504/1/ch01lvl1sec11/making-a-stack
function Stack()
    local out = {}
    -- indexed
    local index = #out
    out.peek = function()
        return out[#out] 
      end
    out.push = function(item)
      out[#out+1] = item
      index = #out
    end
    out.pop = function()
      if #out>0 then
        toReturn = table.remove(out, #out)
        index = #out
        return toReturn
      end

    end
    out.iterator = function()
      return function()
        if index > 0 then
          toReturn = out[index]
          index = index - 1
          return toReturn
        else
          --reset after iterating
          index = #out
        end
      end
    end
    return out
end
--#####################################

--start: Other utils
function split (inputstr, sep)
    if sep == nil then
        -- split on any whitespace    
        sep = "%s"
        --sep = " "
    end
    local t={}
    for str in string.gmatch(inputstr, "([^"..sep.."]+)") do
            table.insert(t, str)
    end
    return t
end

function table2String(table)
    string = ""
    last = #table
    for i=1, #table do
        word = table[i]
        if(i == last) then
            string = string.. word
        else
            string = string.. word .. " "
        end
    end
    return string
end

function ds2String(ds)
  out = ''
  if ds == nil or #ds <= 0 then
    return out
  end
  for element in ds.iterator() do
      out = out .. tostring(element) .. ' '
  end
  return out
end

function trim(s)
  return (string.gsub(s, "^%s*(.-)%s*$", "%1"))
end

--#####################################

---------------------------------- Calculator ----------------------------------
function read()
    io.write('Enter fromula:')
    local fromConsole = io.read("*l")
    fromConsole = split(fromConsole)
    if fromConsole[#fromConsole] ~= '=' then
        table.insert(fromConsole, '=')
    end    
    return fromConsole
end

function sy(tokens)
  -- https://en.wikipedia.org/wiki/Shunting_yard_algorithm
  -- example input: 2 + 5 * ( 4 + 3 ), expected out: 2 5 4 3 + * + 
  -- 3 + 4 × 2 ÷ ( 1 − 5 ) ^ 2 ^ 3, expected out: 3 4 2 × 1 5 − 2 3 ^ ^ ÷ + 
  -- only for ints because of tonumber(...)
    --WIP
    readTokens= {}
    outOueue = Queue()
    opStack = Stack()
    i = 1
    while #readTokens ~= #tokens do
        --current = trim(tokens[i])
        current = tokens[i]
        print("run ".. i .. " of ".. #tokens)
        print(" Current is " .. current)
        if current == '=' then
          print(' skipping ')
          --goto continue
          break
        end
        if  tonumber(current) ~= nil then
            outOueue.add(tonumber(current))
            print(' Add ' .. current .. ' to output!')
        end
        --if tonumber(current) == nil and current ~= '(' and current ~= ')' then
        if tonumber(current) == nil  then
          --print(' Op ' .. current .. ' must NOT be ( or ) here')
            -- only add operators, not parentheses
            while #opStack > 0 and opStack.peek() ~= '(' and (getPrecedence(opStack.peek()) > getPrecedence(current) or (getPrecedence(opStack.peek()) == getPrecedence(current) and getAssociativity(current) == 'left')) do
              dummy = opStack.pop()
              print( ' Pop ' .. dummy .. ' from stack to output!')
              outOueue.add(dummy)
            end
            if current ~= ')' then
              print(' Push ' .. current .. ' to stack!')
              opStack.push(current)
            end
        end
        if current == '(' or current == ')' then
          -- here: not a number and not a operator, so it must be ( or )
          --print(' Op ' .. current .. ' must be ( or ) here')
          if current == '(' then
            --opStack.push(current)
            --print(' Push ' .. current .. ' to stack!')
          else
            --assert(current ~= ')', 'found invalid operator here: ' .. tostring(current))
            while #opStack > 0 and opStack.peek() ~= '(' do
              outOueue.add(opStack.pop())
            end
            -- pop left
            if # opStack > 0 then 
              assert(opStack.peek() == '(', 'operator is ' .. tostring(current) .. ' but should be \'(\'')
              opStack.pop()
            end
          end
        end
        ::continue::
        if #opStack > 0 then print(" Stack: " .. ds2String(opStack)) end
        if #outOueue > 0 then print(" Queue: " .. ds2String(outOueue)) end
        table.insert(readTokens, current)
        i = i + 1
        print('\n')
    end

    while #opStack > 0 do
      outOueue.add(opStack.pop())
    end 
    return outOueue   
end

function getPrecedence(operator)
    if operator == '(' or operator == ')' then
      return 5
    elseif operator == '^' then 
      return 4
    elseif  operator == '×' or operator == '÷' or operator == '*' or operator == '/' then
      return 3
    elseif operator == '+' or operator == '-' or operator == '−' then
      return 2
    else 
        error("invalid operator " .. operator)
    end
end

function getAssociativity(operator)
    if operator == '^' then 
        return 'right'
    elseif operator == '×' or operator == '÷' or operator == '*' or operator == '/' or operator == '+' or operator == '-' or operator == '−' then
        return 'left'
    else 
        error("invalid operator " .. operator)
    end
end

function eval(queue)
  --eval is wrong. should be processed just the other way round
  outStack = Stack()
  numberQueue = Queue()
  current = queue.del()
  while current ~= nil do
    while tonumber(current) ~= nil do
      numberQueue.add(tonumber(current))
      current = queue.del()
    end
    -- Now current can only be an operator
    left = tonumber(numberQueue.del())
    print('Queue ' .. #numberQueue .. ' Stack ' .. #outStack)
    print('current ' .. current)
    if #outStack == 0 then
      right = tonumber(numberQueue.first())
    else
      right = tonumber(outStack.peek())
    end
    print('left ' .. left .. ' right ' .. right)
    erg = nil
    if current == '+' then
      erg = left + right
    elseif current == '-' or current == '−' then
      erg = left - right
    elseif current == '×' or current == '*' then
      erg = left * right
    elseif current == '÷' or current == '/' then
      erg = left / right
    else
      error("invalid operator " .. current) 
    end
    print("intermediate is " .. erg)
    outStack.push(erg)
    if #numberQueue > 0 then print('numberQueue: ' .. ds2String(numberQueue)) end
    if #outStack > 0 then print('outStack: ' .. ds2String(outStack)) end
    current = queue.del()
    print('')
  end
  return outStack

end

--------------------------------- Testing ----------------------------------
--print(table2String(splittedInput))
--print(#splittedInput -1) -- should be 9 (because example is 0 indexed)
--testStack = Stack()
--testStack.push('+')
--testStack.push('x')
--testStack.push('(')
--print(ds2String(testStack))
--print("# Stack size: " .. #testStack)
--for element in testStack.iterator() do
--    print(element)
--end
--print("# Stack size: " .. #testStack)-
--print('----------------------------------------')
--testQueue = Queue()
--testQueue.add(4)
--testQueue.add(8)
--print("# Queue size: " .. #testQueue)
--print(type(testQueue.iterator()))

--------------------------------- Start ----------------------------------
splittedInput = read()
print("After read() " .. table2String(splittedInput))
print('--------------------------------------')
processedInput = sy(splittedInput)
print("After sy() " .. ds2String(processedInput))
print('--------------------------------------')
evaluatedInput = eval(processedInput)
--for 2 5 4 3 + * + After eval should be 7,35,37
print("After eval() " .. ds2String(evaluatedInput))


