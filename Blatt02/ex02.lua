------!!!!!!!!!!!!!!!
----- Array indices start at 1 !!!!
----- some ppl just want to see the world burn ....
------!!!!!!!!!!!!!!!

---------------------------------- Utils ----------------------------------
---- start: Queue, src: https://subscription.packtpub.com/book/game-development/9781849515504/1/ch01lvl1sec12/making-a-queue
local function Queue()
    local out = {}
    local first, last = 0, -1
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
        return out.del()
      end
    end
    out.toString = function()
        -- NOT WOKRING!
        outString = ""
        -- i = start (incl),end (incl), perStep
        print("out size ".. #out)
        print(out[1]) -- why nil?!
        for i = #out,1,-1 do
            outString = outString .. out[i]
            if i ~= 1 then
                outString = outString .. " "
            end
        end
        return outString
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
    out.peek = function()
        return out[#out] 
      end
    out.push = function(item)
      out[#out+1] = item
    end
    out.pop = function()
      if #out>0 then
        return table.remove(out, #out)
      end
    end
    out.iterator = function()
      return function()
        -- clearing stack while iterating!
        return out.pop()
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

--#####################################

---------------------------------- Calculator ----------------------------------
function read()
    io.write('Enter fromula:')
    local fromConsole = io.read("*l")
    fromConsole = split(fromConsole)
    if fromConsole[#fromConsole] ~= '=' then
        table.insert(fromConsole, ' =')
    end    
    return fromConsole
end

function sy(tokens)
    --WIP
    readTokens= {}
    outOueue = Queue()
    opStack =stack()
    i = 1
    while #readTokens ~= #tokens do
        current = tokens[i]
        if  type(current) == 'number' then
            outOueue.add(current)
        else 
            -- if currrent is not a number then it must be a operator
            opStack.push(current)
        end

        
    end    
end

function checkPrecedence(operator)
    if operator == '^' then 
        return 4
    elseif operator == '*' or operator == '/' then
        return 3
    elseif operator == '+' or operator == '-' then
        return 2
    else 
        error("invalid operator " .. operator)
    end
end

function checkAssociativity(operator)
    if operator == '^' then 
        return 'right'
    elseif operator == '*' or operator == '/' or operator == '+' or operator == '-' then
        return 'left'
    else 
        error("invalid operator " .. operator)
    end
end

function eval()

end

--------------------------------- Start ----------------------------------
splittedInput = read()

print(table2String(splittedInput))
print(#splittedInput -1) -- should be 9 (because example is 0 indexed)
testStack = Stack()
testStack.push(1337)
testStack.push(42)
print("# Stack size: " .. #testStack)
--for element in testStack.iterator() do
--    print(element)
--end
--print("# Stack size: " .. #testStack)
print('----------------------------------------')
testQueue = Queue()
testQueue.add(4)
testQueue.add(8)
print("# Queue size: " .. #testQueue)
print(type(testQueue.iterator()))

