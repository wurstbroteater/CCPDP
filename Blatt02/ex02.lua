------!!!!!!!!!!!!!!!
----- Array indices start at 1 !!!!
----- some ppl just want to see the world burn ....
------!!!!!!!!!!!!!!!

---------------------------------- Utils ----------------------------------
---- start: Queue, src: https://subscription.packtpub.com/book/game-development/9781849515504/1/ch01lvl1sec12/making-a-queue
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

--start: Stack, src: https://subscription.packtpub.com/book/game-development/9781849515504/1/ch01lvl1sec11/making-a-stack
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
  if ds == nil or #ds == 0 then
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
  -- example input: 2 + 5 * ( 4 + 3 )
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
        --print(" Stack size " .. #opStack)
        --print(" Queue size " .. #outOueue)
        --print(" Current is " .. current .. " size " .. #current)
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
        if tonumber(current) == nil then
          --print(' Op ' .. current .. ' must NOT be ( or ) here')
            -- only add operators, not parentheses
            while #opStack > 0 and opStack.peek() ~= '(' and (getPrecedence(opStack.peek()) > getPrecedence(current) or (getPrecedence(opStack.peek()) == getPrecedence(current) and getAssociativity(current) == 'left')) do
              outOueue.add(opStack.pop())
            end
            opStack.push(current)
            print(' Push ' .. current .. ' to stack!')
        end
        if current == '(' or current == ')' then
          -- here: not a number and not a operator, so it must be ( or )
          --print(' Op ' .. current .. ' must be ( or ) here')
          if operator == '(' then
            opStack.push(current)
            print(' Push ' .. current .. ' to stack!')
          else
            assert(operator ~= ')', 'found invalid operator here: ' .. tostring(operator))
            while #opStack > 0 and opStack.peek() ~= '(' do
              outOueue.add(opStack.pop())
            end
            -- pop left
            if # opStack > 0 then 
              assert(opStack.peek() == '(', 'operator is ' .. tostring(operator) .. ' but should be \'(\'')
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

function eval()

end

--------------------------------- Testing ----------------------------------
--print(table2String(splittedInput))
--print(#splittedInput -1) -- should be 9 (because example is 0 indexed)
--testStack = Stack()
--testStack.push(1337)
--testStack.push(42)
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
processedInput = sy(splittedInput)
print('--------------------------------------')
print("After sy() " .. ds2String(processedInput))
